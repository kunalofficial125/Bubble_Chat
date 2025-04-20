package com.vision.bubblechat.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.vision.bubblechat.R
import com.vision.bubblechat.adapters.SearchContactsAdapter
import com.vision.bubblechat.data_models.ContactedUserModel
import com.vision.bubblechat.databinding.FragmentAddChatsBinding
import com.vision.bubblechat.viewModels.AddChatsViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddChats.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddChats : Fragment() {
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

    private var _binding: FragmentAddChatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AddChatsViewModel
    private lateinit var recRvAdapter: SearchContactsAdapter
    private val allUsers = mutableListOf<ContactedUserModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddChatsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AddChatsViewModel::class.java]

        binding.recommendationRV.layoutManager = LinearLayoutManager(requireContext())
        recRvAdapter = SearchContactsAdapter(requireActivity(), allUsers)
        binding.recommendationRV.adapter = recRvAdapter

        viewModel.getAllUsersData.observe(viewLifecycleOwner){ result->
            allUsers.clear()
            allUsers.addAll(result)
            var sameUser: ContactedUserModel? = null
            for (user in allUsers){
                Log.d("bothMails", "${user.email} ${FirebaseAuth.getInstance().currentUser?.email.toString()}")
                if(FirebaseAuth.getInstance().currentUser?.email.toString() == user.email){
                    sameUser = user
                }
            }

            if(sameUser != null){
                allUsers.remove(sameUser)
            }

            if(allUsers.isEmpty()){
                binding.noChatsTxtView.visibility = View.VISIBLE
                binding.recommendationRV.visibility = View.GONE
            }

            recRvAdapter.notifyDataSetChanged()
        }

        viewModel.getAllUsers()


        binding.searchView.setOnEditorActionListener{ v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Handle the forward/next button press
                viewModel.getUserBySearch(binding.searchView.text.toString())
                Log.d("search", binding.searchView.text.toString())
                true
            } else {
                false
            }
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }


        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddChats.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddChats().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}