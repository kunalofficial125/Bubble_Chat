package com.vision.bubblechat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vision.bubblechat.MainActivity
import com.vision.bubblechat.R
import com.vision.bubblechat.adapters.ContactedUsersChatAdapter
import com.vision.bubblechat.data_models.ChatModel
import com.vision.bubblechat.data_models.ContactedUserModel
import com.vision.bubblechat.databinding.FragmentHomeScreenBinding
import com.vision.bubblechat.databinding.FragmentLoginPageBinding
import com.vision.bubblechat.viewModels.HomeViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeScreen : Fragment() {
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

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private val lastMessagesList = mutableListOf<ChatModel>()
    private val contactedUsersList = mutableListOf<ContactedUserModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.main.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)

        binding.chatContactsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ContactedUsersChatAdapter(requireActivity(), contactedUsersList, lastMessagesList)
        binding.chatContactsRecyclerView.adapter = adapter

        binding.loadingView.visibility = View.VISIBLE

        viewModel.listOfContactedUsersWithLastMessage.observe(viewLifecycleOwner){
            if(it.first.isNotEmpty()){
                binding.noChatsTxtView.visibility = View.GONE
                contactedUsersList.clear()
                lastMessagesList.clear()
                contactedUsersList.addAll(it.first)
                lastMessagesList.addAll(it.second)
                adapter.notifyDataSetChanged()
            }
            else{
                binding.noChatsTxtView.visibility = View.VISIBLE
            }
            binding.loadingView.visibility = View.GONE
        }

        viewModel.getContactedUsersWithLastMessage()

        binding.addChat.setOnClickListener{
            (activity as MainActivity).replaceFragment(AddChats(), Bundle(), "Add Chats")
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
         * @return A new instance of fragment HomeScreen.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeScreen().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}