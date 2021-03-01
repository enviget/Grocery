package com.example.grocery.models

data class UserResponse(
    val token: String,
    val user: User
)

data class User(
    var __v: Int? = null,
    var createdAt: String? = null,
    var firstName: String?= null,
    var password: String? = null,

    var mobile: String?= null,
    var _id: String?= null,
    var email : String?= null
)