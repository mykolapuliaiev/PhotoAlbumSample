package com.example.photoalbumsample.networking.api

import com.example.photoalbumsample.networking.responses.AlbumsResponse
import com.example.photoalbumsample.networking.responses.PhotosResponse
import com.example.photoalbumsample.networking.responses.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BaseApi {

    @GET("users")
    suspend fun getUsers(): List<UserResponse>

    @GET("albums")
    suspend fun getAlbumsByUser(@Query("userId") userId: Int): List<AlbumsResponse>

    @GET("photos")
    suspend fun getPhotosByAlbum(@Query("albumId") albumId: Int): List<PhotosResponse>
}