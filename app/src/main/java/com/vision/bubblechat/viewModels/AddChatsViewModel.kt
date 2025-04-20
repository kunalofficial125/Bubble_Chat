package com.vision.bubblechat.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vision.bubblechat.data_models.UserModel
import com.vision.bubblechat.repositories.ChatRepository

class AddChatsViewModel: ViewModel() {

    private val chatRepository = ChatRepository()

    val getAllUsersData = chatRepository.getAllUsersResult

    fun getAllUsers(){
        chatRepository.getAllUsers()
    }

    fun getUserBySearch(query: String){
        chatRepository.searchUser(query)
    }

}