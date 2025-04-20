package com.vision.bubblechat.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import com.vision.bubblechat.MainActivity
import com.vision.bubblechat.R
import com.vision.bubblechat.data_models.ContactedUserModel
import com.vision.bubblechat.data_models.UserModel
import com.vision.bubblechat.databinding.FragmentLoginPageBinding
import com.vision.bubblechat.databinding.FragmentSignUpPageBinding
import com.vision.bubblechat.viewModels.AuthViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUp_Page.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUp_Page : Fragment() {
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

    private var _binding: FragmentSignUpPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel

    private lateinit var accountSharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpPageBinding.inflate(inflater, container, false)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.parent.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)

        accountSharedPreferences = requireActivity().getSharedPreferences("account", Context.MODE_PRIVATE)

        authViewModel.checkUserExistTask.observe(viewLifecycleOwner){ result ->
            if(result){
                binding.emailInput.error = "User already exists"
                binding.loadingView.visibility = View.GONE
            } else {
                authViewModel.createUserWithEmail(
                    binding.emailInput.text.toString(),
                    binding.passwordInput.text.toString()
                )
            }
        }

        authViewModel.signUpTask.observe(viewLifecycleOwner){
            if(it){
                authViewModel.createUserInDatabase(
                    UserModel(
                        binding.nameInput.text.toString(),
                        binding.emailInput.text.toString(),
                        binding.usernameInput.text.toString(),
                        mutableListOf()
                    ))

                binding.loadingView.visibility = View.GONE
                accountSharedPreferences.edit().putString("email", binding.emailInput.text.toString()).apply()
                accountSharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            }
            else{
                binding.emailInput.error = ""
                binding.loadingView.visibility = View.GONE
            }
        }

        binding.signupButton.setOnClickListener {
            if(verifyDetails()){
                binding.loadingView.visibility = View.VISIBLE
                authViewModel.checkUserExist(binding.emailInput.text.toString())
            }
        }


        binding.loginRedirectButton.setOnClickListener{
            requireActivity().onBackPressed()
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
        if(binding.passwordInput.text.toString().length < 6){
            binding.passwordInput.error = "Password must be at least 6 characters"
            return false
        }
        if(binding.nameInput.text.toString().isEmpty()){
            binding.nameInput.error = "Name cannot be empty"
            return false
        }
        if(binding.usernameInput.text.toString().isEmpty()){
            binding.usernameInput.error = "Username cannot be empty"
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
         * @return A new instance of fragment SignUp_Page.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUp_Page().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}