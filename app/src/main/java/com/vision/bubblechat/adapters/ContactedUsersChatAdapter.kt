package com.vision.bubblechat.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.collection.LLRBNode
import com.vision.bubblechat.MainActivity
import com.vision.bubblechat.R
import com.vision.bubblechat.data_models.ChatModel
import com.vision.bubblechat.data_models.ContactedUserModel
import com.vision.bubblechat.fragments.ChatScreen
import com.vision.bubblechat.helpers.CommonHelper

class ContactedUsersChatAdapter(private val activity: Activity, private val listOfContactedUsers: MutableList<ContactedUserModel>, private val listOfLastMessages: MutableList<ChatModel>): RecyclerView.Adapter<ContactedUsersChatAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.imageProfile)
        val firstLetterOfName: TextView = itemView.findViewById(R.id.firstLetterOfName)
        val name: TextView = itemView.findViewById(R.id.textName)
        val time: TextView = itemView.findViewById(R.id.textTime)
        val lastMessage:TextView = itemView.findViewById(R.id.textMessage)
        val main: ConstraintLayout = itemView.findViewById(R.id.main)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactedUsersChatAdapter.ViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.chat_list_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactedUsersChatAdapter.ViewHolder, position: Int) {
        val contactedUser = listOfContactedUsers[position]
        val lastMessage = listOfLastMessages[position]

        holder.name.text = contactedUser.username
        holder.time.text = CommonHelper.convertMillisToDateString(lastMessage.timestamp)
        holder.lastMessage.text = lastMessage.message

        holder.firstLetterOfName.text = contactedUser.username.uppercase().first().toString()

        if(lastMessage.timestamp == 0L){
            holder.time.visibility = View.GONE
        }
        else{
            holder.time.visibility = View.VISIBLE
        }

        holder.main.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("otherUserEmail", contactedUser.email)
            bundle.putString("otherUserName", contactedUser.username)
            (activity as MainActivity).replaceFragment(ChatScreen(), bundle, "Chat Screen")
        }

    }

    override fun getItemCount(): Int {
        return listOfContactedUsers.size
    }


    private fun getRandomColor(): Int{
        val colors = listOf(
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.YELLOW,
            Color.DKGRAY
        )
        return colors.random()

    }

}