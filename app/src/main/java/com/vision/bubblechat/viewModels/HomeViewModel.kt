package com.vision.bubblechat.viewModels

import androidx.lifecycle.ViewModel
import com.vision.bubblechat.repositories.ChatRepository

class HomeViewModel: ViewModel() {

    private val chatRepo = ChatRepository()

    val listOfContactedUsersWithLastMessage = chatRepo.listOfContactedUsersWithLastMessage

    fun getContactedUsersWithLastMessage(){
        chatRepo.getContactedUsersWithLastMessage()
    }

}