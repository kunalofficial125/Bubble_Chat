package com.vision.bubblechat.adapters

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vision.bubblechat.MainActivity
import com.vision.bubblechat.R
import com.vision.bubblechat.data_models.ContactedUserModel
import com.vision.bubblechat.fragments.ChatScreen
import com.vision.bubblechat.repositories.ChatRepository

class SearchContactsAdapter(private val activity:Activity, private val contactsList: List<ContactedUserModel>): RecyclerView.Adapter<SearchContactsAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageProfile = itemView.findViewById<ImageView>(R.id.imageProfile)
        val firstLetterOfName = itemView.findViewById<TextView>(R.id.firstLetterOfName)
        val textName = itemView.findViewById<TextView>(R.id.textName)
        val textUsername = itemView.findViewById<TextView>(R.id.textUsername)
        val main = itemView.findViewById<View>(R.id.main)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchContactsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_user_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchContactsAdapter.ViewHolder, position: Int) {
        val currentItem = contactsList[position]
        holder.firstLetterOfName.text = currentItem.username.uppercase().first().toString()
        holder.textName.text = currentItem.username
        holder.textUsername.text = currentItem.email

        holder.main.setOnClickListener {
            ChatRepository().addContactedUser(ContactedUserModel(currentItem.username, currentItem.email))

            val bundle = Bundle()
            bundle.putString("otherUserName", currentItem.username)
            bundle.putString("otherUserEmail", currentItem.email)
            (activity as MainActivity).replaceFragment(ChatScreen(), bundle, "Chat Screen")
        }

    }

    override fun getItemCount(): Int {
        return contactsList.size
    }
}