package com.vision.bubblechat.viewModels

import androidx.lifecycle.ViewModel
import com.vision.bubblechat.data_models.ChatModel
import com.vision.bubblechat.data_models.ContactedUserModel
import com.vision.bubblechat.repositories.ChatRepository

class ChatScreenViewModel: ViewModel() {

    private val chatRepository = ChatRepository()

    val chatListData = chatRepository.chatListData
    val addChatMessageStatus = chatRepository.addChatMessageStatus


    fun getAllChats(chatID: String){
        chatRepository.fetchUserChats(chatID)
    }

    fun addChatMessage(chatID: String, chatModel: ChatModel, otherUserEmail: String, myUsername: String){
        chatRepository.addChatMessage(chatID, chatModel, otherUserEmail, myUsername)
    }

}