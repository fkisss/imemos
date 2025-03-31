package com.imemos.data.model

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("username")
    val username: String,
    
    @SerializedName("password")
    val password: String
)

data class AuthResponse(
    @SerializedName("token")
    val token: String,
    
    @SerializedName("user")
    val user: User
)