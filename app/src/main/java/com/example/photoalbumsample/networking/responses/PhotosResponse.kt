package com.example.photoalbumsample.networking.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotosResponse(
    val albumId: Int?,
    val id: Int?,
    val thumbnailUrl: String,
    val title: String?,
    val url: String
)