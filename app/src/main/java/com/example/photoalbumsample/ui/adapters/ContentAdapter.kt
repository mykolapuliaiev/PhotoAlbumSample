package com.example.photoalbumsample.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.photoalbumsample.databinding.ItemAlbumBinding
import com.example.photoalbumsample.databinding.ItemPhotoBinding
import com.example.photoalbumsample.databinding.ItemUserBinding
import com.example.photoalbumsample.models.ContentModel

class ContentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        /**
         * View types constants for content.
         */
        private const val TYPE_USER = 1
        private const val TYPE_ALBUM = 2
        private const val TYPE_PHOTO = 3
    }

    private var items: MutableList<ContentModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_USER -> {
                val binding = ItemUserBinding.inflate(inflater, parent, false)
                UserViewHolder(binding)
            }
            TYPE_ALBUM -> {
                val binding = ItemAlbumBinding.inflate(inflater, parent, false)
                AlbumViewHolder(binding)
            }
            else -> {
                val binding = ItemPhotoBinding.inflate(inflater, parent, false)
                PhotoViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_USER -> (holder as UserViewHolder).bind(items[position])
            TYPE_ALBUM -> (holder as AlbumViewHolder).bind(items[position])
            TYPE_PHOTO -> (holder as PhotoViewHolder).bind(items[position])
        }
    }

    fun setItems(items: MutableList<ContentModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun getItem(position: Int) = items[position]

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ContentModel.User -> TYPE_USER
            is ContentModel.Album -> TYPE_ALBUM
            is ContentModel.Photo -> TYPE_PHOTO
        }
    }

    /**
     * View holder for user items.
     */
    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(content: ContentModel) {
            binding.user = content as ContentModel.User
        }
    }

    /**
     * View holder for album items.
     */
    inner class AlbumViewHolder(private val binding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(content: ContentModel) {
            binding.album = content as ContentModel.Album
        }
    }

    /**
     * View holder for photo items.
     */
    inner class PhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(content: ContentModel) {
            val photo = content as ContentModel.Photo

            val url = getGlideUrl(photo.imageUrl)
            val thumbnailUrl = getGlideUrl(photo.thumbnailUrl)

            Glide.with(binding.userPhoto)
                .load(url)
                .thumbnail(Glide.with(binding.userPhoto).load(thumbnailUrl))
                .into(binding.userPhoto)
        }

        /**
         * We should use custom user agent for images from current API.
         *
         * It has to be used with every image URL for current API or image won't be loaded.
         */
        private fun getGlideUrl(url: String) = GlideUrl(
            url, LazyHeaders.Builder()
                .addHeader(
                    "User-Agent",
                    WebSettings.getDefaultUserAgent(binding.userPhoto.context)
                )
                .build()
        )
    }
}