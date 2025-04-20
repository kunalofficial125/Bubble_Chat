package com.vision.bubblechat.helpers

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommonHelper {

    companion object{
        fun sanitizeEmailForFirebase(email: String?): String? {
            return email?.replace(".", "_")?.replace("@", "_")
        }

        fun generateGroupChatID(user1: String, user2: String): String {
            val chatId = if (user1.compareTo(user2) < 0) user1 + "_" + user2 else user2 + "_" + user1
            return chatId
        }

        fun convertMillisToDateString(millis: Long): String {
            val sdf = SimpleDateFormat("HH:mm dd MMM", Locale.getDefault())
            val date = Date(millis)
            return sdf.format(date)
        }


    }

}