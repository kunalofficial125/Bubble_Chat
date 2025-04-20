package com.vision.bubblechat.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.vision.bubblechat.MainActivity
import com.vision.bubblechat.R
import com.vision.bubblechat.activities.InitialActivity
import com.vision.bubblechat.databinding.FragmentLoginPageBinding
import com.vision.bubblechat.viewModels.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginPage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentLoginPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel

    private lateinit var accountSharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginPageBinding.inflate(inflater, container, false)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.main.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)

        lifecycleScope.launch {
            delay(1500)
            binding.lottieAnim.playAnimation()
        }

        accountSharedPreferences = requireActivity().getSharedPreferences("account", Context.MODE_PRIVATE)

        binding.signUpButton.setOnClickListener{
            (activity as InitialActivity).replaceFragment(SignUp_Page(), Bundle(), "Sign Up Page")
        }

        authViewModel.signInTask.observe(viewLifecycleOwner) {
            if (it == "successful") {
                Log.d("isUserSignedIn", "successful")
                accountSharedPreferences.edit().putString("email", binding.emailInput.text.toString()).apply()
                accountSharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                binding.loadingView.visibility = View.GONE
                binding.passwordInput.error = "Incorrect Password"
            }
        }


        binding.loginButton.setOnClickListener {
            if (verifyDetails()) {
                Log.d("signInMethodCalled", "done")
                binding.loadingView.visibility = View.VISIBLE
                authViewModel.signInUserWithEmail(
                    binding.emailInput.text.toString(),
                    binding.passwordInput.text.toString()
                )
            }
            else{
                Log.d("signInMethodCalled", "failed")
            }
        }

        return binding.root
    }


    private fun verifyDetails(): Boolean{
        if(binding.emailInput.text.toString().isEmpty()){
            binding.emailInput.error = "Email cannot be empty"
            return false
        }
        if(binding.passwordInput.text.toString().isEmpty()){
            binding.passwordInput.error = "Password cannot be empty"
            return false
        }

        return true
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}