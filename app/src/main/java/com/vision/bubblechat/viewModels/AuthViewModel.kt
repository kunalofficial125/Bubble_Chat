package com.vision.bubblechat.viewModels

import androidx.lifecycle.ViewModel
import com.vision.bubblechat.authentication.EmailAuth
import com.vision.bubblechat.data_models.UserModel
import com.vision.bubblechat.repositories.UsersRepository

class AuthViewModel: ViewModel() {
    private val usersRepository = UsersRepository()
    private val emailAuth = EmailAuth()

    val checkUserExistTask = usersRepository.checkUserExistTask
    val signInTask = emailAuth.signInTask
    val signUpTask = emailAuth.signUpTask
    val forgotPasswordTask = emailAuth.forgotPasswordTask

    //Check User Exist
    fun checkUserExist(defaultEmail: String){
        usersRepository.checkUserExist(defaultEmail)
    }

    //Create User in Database
    fun createUserInDatabase(userModel: UserModel){
        usersRepository.createUserInDatabase(userModel)
    }

    //Email Authentication
    fun createUserWithEmail(email: String, password: String){
        emailAuth.createUserWithEmail(email, password)
    }

    //Sign In User
    fun signInUserWithEmail(email: String, password: String){
        emailAuth.signInUserWithEmail(email, password)
    }

    //Send Password Reset Link
    fun sendPasswordResetLinkToUser(email: String){
        emailAuth.sendPasswordResetLinkToUser(email)
    }

}