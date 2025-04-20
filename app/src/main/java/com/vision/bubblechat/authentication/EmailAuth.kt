package com.vision.bubblechat.authentication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class EmailAuth {

    private val auth = FirebaseAuth.getInstance()

    private val _signInTask = MutableLiveData<String>()
    val signInTask: MutableLiveData<String>
        get() = _signInTask

    private val _signUpTask = MutableLiveData<Boolean>()
    val signUpTask: MutableLiveData<Boolean>
        get() = _signUpTask

    private val _forgotPasswordTask = MutableLiveData<Boolean>()
    val forgotPasswordTask: MutableLiveData<Boolean>
        get() = _forgotPasswordTask



    fun createUserWithEmail(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            _signUpTask.postValue(true)
        }.addOnFailureListener {
            _signUpTask.postValue(false)
        }
    }

    fun signInUserWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            _signInTask.postValue("successful")
            Log.d("isUserSignedIn", "successful")
        }.addOnFailureListener { error->
            _signInTask.postValue(error.message.toString())
            Log.d("isUserSignedIn", "failed: ${error.message}")
        }
    }

    fun sendPasswordResetLinkToUser(email: String) {
        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            _forgotPasswordTask.postValue(true)
        }.addOnFailureListener {
            _forgotPasswordTask.postValue(false)
        }
    }

}