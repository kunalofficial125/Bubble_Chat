package com.vision.bubblechat.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.vision.bubblechat.R
import com.vision.bubblechat.adapters.ChatAdapter
import com.vision.bubblechat.data_models.ChatModel
import com.vision.bubblechat.data_models.ContactedUserModel
import com.vision.bubblechat.databinding.FragmentChatScreenBinding
import com.vision.bubblechat.helpers.CommonHelper
import com.vision.bubblechat.viewModels.ChatScreenViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatScreen : Fragment() {
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

    private var _binding: FragmentChatScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatScreenViewModel
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatScreenBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ChatScreenViewModel::class.java)

        val otherUserMail = arguments?.getString("otherUserEmail")
        val otherUserUserName = arguments?.getString("otherUserName")
        binding.otherPersonName.text = otherUserUserName

        val myUsername = activity?.getSharedPreferences("account", Context.MODE_PRIVATE)?.getString("username", "username")

        val chatID = CommonHelper.generateGroupChatID(CommonHelper.sanitizeEmailForFirebase(FirebaseAuth.getInstance().currentUser?.email!!)!!,
            CommonHelper.sanitizeEmailForFirebase(otherUserMail!!)!!)

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatAdapter = ChatAdapter()
        binding.chatRecyclerView.adapter = chatAdapter

        binding.loadingView.visibility = View.VISIBLE

        viewModel.chatListData.observe(viewLifecycleOwner){ result ->
            if(result != null){
                chatAdapter.submitList(result)
                binding.chatRecyclerView.post {
                    binding.chatRecyclerView.scrollToPosition(result.size - 1)
                }
                binding.loadingView.visibility = View.GONE
            }
            else{
                Toast.makeText(requireContext(), "Network Error", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getAllChats(chatID)

        binding.sendButton.setOnClickListener {
            val message = binding.messageEditText.text.toString()
            if(message.isNotEmpty()){
                viewModel.addChatMessage(chatID, ChatModel(FirebaseAuth.getInstance().currentUser?.email!!, message, System.currentTimeMillis()), otherUserMail, myUsername!!)
                binding.messageEditText.text.clear()
            }
        }

        binding.backBtn.setOnClickListener{
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
         * @return A new instance of fragment ChatScreen.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatScreen().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}