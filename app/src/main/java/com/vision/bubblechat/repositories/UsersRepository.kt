package com.vision.bubblechat.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.vision.bubblechat.data_models.UserModel
import com.vision.bubblechat.helpers.CommonHelper

class UsersRepository {

    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.getReference("root").child("users_data")

    private val _checkUserExistTask = MutableLiveData<Boolean>()
    val checkUserExistTask: MutableLiveData<Boolean>
        get() = _checkUserExistTask


    fun checkUserExist(defaultEmail: String) {
        val email: String = if(defaultEmail==""){
            user?.email!!
        } else{
            defaultEmail
        }

        Log.d("defaultEmail", "email: $email")
        userRef.child(CommonHelper.sanitizeEmailForFirebase(email)!!).get().addOnSuccessListener {
            if(it.exists()){
                _checkUserExistTask.postValue(true)
                Log.d("IsUserExist", "User exists")
            } else{
                _checkUserExistTask.postValue(false)
                Log.d("IsUserExist", "User does not exist")
            }
        }.addOnFailureListener{error->
            Log.d("IsUserExist", error.message.toString())
        }
    }

    fun createUserInDatabase(userModel: UserModel){
        userRef.child(CommonHelper.sanitizeEmailForFirebase(userModel.email)!!).setValue(userModel).addOnSuccessListener {
            Log.d("isUserCreatedInDatabase", "User created in database")
        }.addOnFailureListener{ failure ->
            failure.message?.let { Log.d("isUserCreatedInDatabase", it) }
        }
    }

    fun getUsername(email: String, callback: (String?) -> Unit){
        userRef.child(CommonHelper.sanitizeEmailForFirebase(email)!!).child("username").get().addOnSuccessListener {
            val username = it.getValue(String::class.java)
            callback(username)
            Log.d("isUsernameFetching", username.toString())
        }.addOnFailureListener{ failure ->
            failure.message?.let { Log.d("isUsernameFetching", it) }
            callback("null")
        }
    }


}