package com.example.photoalbumsample.models

sealed class ContentModel {
    data class User(val id: Int, val name: String, val company: String, val email: String) :
        ContentModel()

    data class Album(val id: Int, val name: String) : ContentModel()
    data class Photo(val thumbnailUrl: String, val imageUrl: String) : ContentModel()
}