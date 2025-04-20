package com.vision.bubblechat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.vision.bubblechat.R
import com.vision.bubblechat.data_models.ChatModel
import com.vision.bubblechat.helpers.CommonHelper

class ChatAdapter : ListAdapter<ChatModel, RecyclerView.ViewHolder>(DiffCallback()) {

    private val VIEW_TYPE_CURRENT_USER = 1
    private val VIEW_TYPE_OTHER_USER = 2

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).senderID == FirebaseAuth.getInstance().currentUser?.email) {
            VIEW_TYPE_CURRENT_USER
        } else {
            VIEW_TYPE_OTHER_USER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CURRENT_USER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_sent, parent, false)
            CurrentUserViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_received, parent, false)
            OtherUserViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        if (holder is CurrentUserViewHolder) {
            holder.messageTextView.text = message.message
            holder.dateAndTime.text = CommonHelper.convertMillisToDateString(message.timestamp)
        } else if (holder is OtherUserViewHolder) {
            holder.messageTextView.text = message.message
            holder.dateAndTime.text = CommonHelper.convertMillisToDateString(message.timestamp)
        }
    }

    class CurrentUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.messageTextView)
        val dateAndTime: TextView = view.findViewById(R.id.dateAndTime)
    }

    class OtherUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.messageTextView)
        val dateAndTime: TextView = view.findViewById(R.id.dateAndTime)
    }

    class DiffCallback : DiffUtil.ItemCallback<ChatModel>() {
        override fun areItemsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
            return oldItem.timestamp == newItem.timestamp && oldItem.message == newItem.message
        }

        override fun areContentsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
            return oldItem == newItem
        }
    }
}