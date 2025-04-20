package com.vision.bubblechat.data_models

data class ChatModel(
    var senderID: String,
    var message: String,
    var timestamp: Long
) {
    constructor() : this("", "", 0)

}