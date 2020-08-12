package com.example.photoalbumsample.networking.api

import com.example.photoalbumsample.networking.ApiFactory
import com.example.photoalbumsample.networking.ResultWrapper
import com.example.photoalbumsample.networking.responses.AlbumsResponse
import com.example.photoalbumsample.networking.responses.PhotosResponse
import com.example.photoalbumsample.networking.responses.UserResponse
import kotlinx.coroutines.Dispatchers

class BaseRepository {
    private var client: BaseApi = ApiFactory.retrofit().create(BaseApi::class.java)

    suspend fun getUsers(): ResultWrapper<List<UserResponse>> =
        ResultWrapper.safeApiCall(Dispatchers.IO) {
            client.getUsers()
        }

    suspend fun getAlbumsByUser(userId: Int): ResultWrapper<List<AlbumsResponse>> =
        ResultWrapper.safeApiCall(Dispatchers.IO) {
            client.getAlbumsByUser(userId)
        }

    suspend fun getPhotosByAlbum(albumId: Int): ResultWrapper<List<PhotosResponse>> =
        ResultWrapper.safeApiCall(Dispatchers.IO) {
            client.getPhotosByAlbum(albumId)
        }
}