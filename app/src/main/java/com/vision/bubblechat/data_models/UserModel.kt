package com.vision.bubblechat.data_models

data class UserModel(
    var name: String,
    var email: String,
    var username: String,
    var contactedUsers: List<ContactedUserModel>
) {
    constructor() : this("", "", "", emptyList())
}

data class ContactedUserModel(
    var username: String,
    var email: String,

){
    constructor() : this("", "")

}