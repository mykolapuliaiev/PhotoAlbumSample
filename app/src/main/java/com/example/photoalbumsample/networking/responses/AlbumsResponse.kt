package com.example.photoalbumsample.networking.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumsResponse(val id: Int, val title: String, val userId: Int?)