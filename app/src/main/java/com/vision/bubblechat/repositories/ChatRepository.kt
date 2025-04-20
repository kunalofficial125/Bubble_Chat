package com.vision.bubblechat.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vision.bubblechat.data_models.ChatModel
import com.vision.bubblechat.data_models.ContactedUserModel
import com.vision.bubblechat.data_models.UserModel
import com.vision.bubblechat.helpers.CommonHelper
import java.util.UUID

class ChatRepository {

    private val database = FirebaseDatabase.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser
    private val allUsersPath = database.getReference("root/users_data")
    private val contactedUsersPath = database.getReference("root/users_data")
        .child(CommonHelper.sanitizeEmailForFirebase(user?.email!!)!!).
        child("contactedUsers")
    private val contactedUserChatPath = database.getReference("root/chats")

    //Live Data
    private val _listOfContactedUsersWithLastMessage = MutableLiveData<Pair<List<ContactedUserModel>, List<ChatModel> >>()
    val listOfContactedUsersWithLastMessage: LiveData<Pair<List<ContactedUserModel>, List<ChatModel> >> get() = _listOfContactedUsersWithLastMessage

    private val _addContactedUserStatus = MutableLiveData<Boolean>()
    val addContactedUserStatus: LiveData<Boolean> get() = _addContactedUserStatus

    private val _getAllUsersResult = MutableLiveData<List<ContactedUserModel>>()
    val getAllUsersResult: LiveData<List<ContactedUserModel>> get() = _getAllUsersResult

    private val _chatListData = MutableLiveData<List<ChatModel>>()
    val chatListData: LiveData<List<ChatModel>> get() = _chatListData

    private val _addChatMessageStatus = MutableLiveData<Boolean>()
    val addChatMessageStatus: LiveData<Boolean> get() = _addChatMessageStatus


    // I am reusing ContactedUserModel as a UserModel for receiving only important data
    fun getAllUsers(){
        val users = mutableListOf<ContactedUserModel>()
        allUsersPath.get().addOnSuccessListener { snapshot ->
            for (userSnapshot in snapshot.children) {
                val user = userSnapshot.getValue(ContactedUserModel::class.java)
                if (user != null) {
                    users.add(user)
                }
            }
            _getAllUsersResult.postValue(users)
        }
    }


    fun addContactedUser(contactedUser: ContactedUserModel) {
        contactedUsersPath.child(CommonHelper.sanitizeEmailForFirebase(contactedUser.email)!!).setValue(contactedUser).addOnSuccessListener {
            _addContactedUserStatus.postValue(true)
        }.addOnFailureListener {
            _addContactedUserStatus.postValue(false)
        }
    }

    fun getContactedUsersWithLastMessage() {
        val contactedUsers = mutableListOf<ContactedUserModel>()
        contactedUsersPath.get().addOnSuccessListener { snapshot ->
            for (userSnapshot in snapshot.children) {
                val contactedUser = userSnapshot.getValue(ContactedUserModel::class.java)
                if (contactedUser != null) {
                    contactedUsers.add(contactedUser)
                }
            }

            val tasks = mutableListOf<Task<DataSnapshot>>()
            val lastMessageMapping = mutableMapOf<String, ChatModel>()

            for (contactedUser in contactedUsers) {
                val chatID = CommonHelper.generateGroupChatID(CommonHelper.sanitizeEmailForFirebase(user?.email!!)!!, CommonHelper.sanitizeEmailForFirebase(contactedUser.email)!!)
                val task = contactedUserChatPath.child(chatID).child("lastMessage").get()
                tasks.add(task)

                task.addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        lastMessageMapping[contactedUser.email] = snapshot.getValue(ChatModel::class.java)!!
                    } else {
                        lastMessageMapping[contactedUser.email] = ChatModel("", "Tap to chat", 0)
                    }
                }
            }

            Tasks.whenAllComplete(tasks).addOnCompleteListener {
                // All tasks are done — success or fail
                onAllChatsFetched(lastMessageMapping, contactedUsers)
            }
        }
    }


    private fun onAllChatsFetched(lastMessageMapping: Map<String, ChatModel>, contactedUsers: List<ContactedUserModel>) {
        val sortedLastMessages = mutableListOf<ChatModel>()
        for(contactedUser in contactedUsers){
            sortedLastMessages.add(lastMessageMapping[contactedUser.email]!!)
        }
        _listOfContactedUsersWithLastMessage.postValue(Pair(contactedUsers, sortedLastMessages))
    }

    fun fetchUserChats(chatID: String){
        contactedUserChatPath.child(chatID).child("messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatList = mutableListOf<ChatModel>()
                for (chatSnapshot in snapshot.children) {
                    val chat = chatSnapshot.getValue(ChatModel::class.java)
                    if (chat != null) {
                        chatList.add(chat)
                    }
                }
                _chatListData.postValue(chatList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("firebase", error.message.toString())
            }

        })

    }

    fun addChatMessage(chatID: String, chatModel: ChatModel, otherUserEmail: String, myUsername: String){
        contactedUserChatPath.child(chatID).child("messages").push().setValue(chatModel).addOnSuccessListener {
            contactedUserChatPath.child(chatID).child("lastMessage").setValue(chatModel).addOnSuccessListener {

                    allUsersPath.child(CommonHelper.sanitizeEmailForFirebase(otherUserEmail)!!).child("contactedUsers")
                        .child(CommonHelper.sanitizeEmailForFirebase(chatModel.senderID)!!)
                        .setValue(ContactedUserModel(myUsername, chatModel.senderID))
                        .addOnSuccessListener {

                        _addChatMessageStatus.postValue(true)
                    }.addOnFailureListener{
                        _addChatMessageStatus.postValue(false)
                    }

                }.addOnFailureListener {
                    _addChatMessageStatus.postValue(false)
                }
            }.addOnFailureListener {
                _addChatMessageStatus.postValue(false)
        }
    }

    // Search by email
    fun searchByEmail(email: String, callback: (ContactedUserModel?) -> Unit) {
        allUsersPath.orderByChild("email").equalTo(email)
            .get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.children.firstOrNull()?.getValue(ContactedUserModel::class.java)
                callback(user)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    // Search by username
    fun searchByUsername(username: String, callback: (ContactedUserModel?) -> Unit) {
        allUsersPath.orderByChild("username").equalTo(username)
            .get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.children.firstOrNull()?.getValue(ContactedUserModel::class.java)
                callback(user)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    // Combined search function
    fun searchUser(query: String) {
        allUsersPath.orderByChild("email").equalTo(query).get().addOnSuccessListener {
            if(it.exists()){
                val user = it.children.firstOrNull()?.getValue(ContactedUserModel::class.java)
                _getAllUsersResult.postValue(listOf(user!!))
            }
            else{
                allUsersPath.orderByChild("username").equalTo(query).get().addOnSuccessListener {
                    if(it.exists()){
                        val user = it.children.firstOrNull()?.getValue(ContactedUserModel::class.java)
                        _getAllUsersResult.postValue(listOf(user!!))
                    }
                    else{
                        Log.d("searchUser","User does not exist")
                    }
                }.addOnFailureListener{ error->
                    Log.d("searchUser",error.message.toString())
                }
            }
        }.addOnFailureListener{ error->
            Log.d("searchUser",error.message.toString())
        }
    }




}