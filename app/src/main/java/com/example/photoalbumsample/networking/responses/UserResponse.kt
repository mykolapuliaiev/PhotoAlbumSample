package com.example.photoalbumsample.networking.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    val company: Company,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String?,
    val username: String?,
    val website: String?
)

@JsonClass(generateAdapter = true)
data class Company(
    val bs: String?,
    val catchPhrase: String?,
    val name: String
)