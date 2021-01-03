package com.benaya.yaniv.model.network

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(val userData: UserData)

@Serializable
data class UserData(
    val id: String,
    //val points: Int,
    val name: String,
    val apikey: String
    // Do we need user's email? It may be ignored in implementation using Json { ignoreUnknownKeys = true }
)
