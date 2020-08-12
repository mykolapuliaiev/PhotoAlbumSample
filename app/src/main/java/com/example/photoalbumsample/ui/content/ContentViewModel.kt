package com.example.photoalbumsample.ui.content

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photoalbumsample.models.ContentModel
import com.example.photoalbumsample.networking.api.BaseRepository
import com.example.photoalbumsample.utils.doForAll
import com.example.photoalbumsample.utils.isError
import com.example.photoalbumsample.utils.isSuccess
import kotlinx.coroutines.launch

class ContentViewModel : ViewModel() {

    companion object {
        private val TAG = this::class.java.simpleName
    }

    private val repository = BaseRepository()
    var currentTab = 0
    var currentUserID = 1
    var currentAlbumID = 1
    var currentPhotosChunk = 0

    var photosChunked: List<List<ContentModel.Photo>> = listOf()

    /**
     * LiveData declaration
     */
    val usersLiveData: MutableLiveData<List<ContentModel.User>> by lazy {
        MutableLiveData<List<ContentModel.User>>()
    }

    val albumsLiveData: MutableLiveData<List<ContentModel.Album>> by lazy {
        MutableLiveData<List<ContentModel.Album>>()
    }

    val photosLiveData: MutableLiveData<List<ContentModel.Photo>> by lazy {
        MutableLiveData<List<ContentModel.Photo>>()
    }

    val showProgress: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    private fun showProgress() {
        showProgress.postValue(true)
    }

    private fun hideProgress() {
        showProgress.postValue(false)
    }

    private fun sendRequest(progress: Boolean = true, request: suspend () -> Unit) {
        viewModelScope.launch {
            if (progress) {
                showProgress()
            }
            request()
        }
    }

    /**
     * Request for fetching users data.
     */
    fun getUsers() {
        sendRequest {
            repository.getUsers()
                .doForAll {
                    hideProgress()
                }.isSuccess { response ->
                    val usersList = response.map { user ->
                        ContentModel.User(user.id, user.name, user.company.name, user.email)
                    }
                    usersLiveData.postValue(usersList)
                }.isError { code, message ->
                    Log.e(TAG, "Error while fetching users. Reason: $message, code: $code")
                }
        }
    }

    /**
     * Request for fetching albums data by user ID.
     */
    fun getAlbumsByID(userId: Int) {
        sendRequest {
            repository.getAlbumsByUser(userId)
                .doForAll {
                    hideProgress()
                }.isSuccess { response ->
                    val albumsList = response.map { album ->
                        ContentModel.Album(album.id, album.title)
                    }
                    albumsLiveData.postValue(albumsList)
                }.isError { code, message ->
                    Log.e(TAG, "Error while fetching albums. Reason: $message, code: $code")
                }
        }
    }

    /**
     * Request for fetching photos data by album ID.
     */
    fun getPhotosByID(albumId: Int) {
        sendRequest {
            repository.getPhotosByAlbum(albumId)
                .doForAll {
                    hideProgress()
                }.isSuccess { response ->
                    val photosList = response.map { photo ->
                        ContentModel.Photo(photo.thumbnailUrl, photo.url)
                    }
                    photosLiveData.postValue(photosList)
                }.isError { code, message ->
                    Log.e(TAG, "Error while fetching photos. Reason: $message, code: $code")
                }
        }
    }
}