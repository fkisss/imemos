package com.imemos.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("username")
    val username: String,
    
    @SerializedName("role")
    val role: String,
    
    @SerializedName("email")
    val email: String?,
    
    @SerializedName("nickname")
    val nickname: String?,
    
    @SerializedName("avatarUrl")
    val avatarUrl: String?,
    
    @SerializedName("createdTs")
    val createdTs: Long,
    
    @SerializedName("updatedTs")
    val updatedTs: Long
)