package com.example.photoalbumsample.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.photoalbumsample.R
import com.example.photoalbumsample.databinding.ActivityMainBinding
import com.example.photoalbumsample.ui.adapters.MainViewPagerAdapter
import com.example.photoalbumsample.utils.Constants.Companion.TAB_ALBUM
import com.example.photoalbumsample.utils.Constants.Companion.TAB_PHOTO
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    companion object {
        /**
         * Values for tabs titles.
         */
        private val tabs = listOf("USERS", "ALBUMS", "PHOTOS")
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainViewPagerAdapter
    private var currentUserId = 1
    private var currentAlbumId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // DataBinding setup
        val layout = R.layout.activity_main
        binding = DataBindingUtil.setContentView(this, layout)

        setupUI()
    }

    /**
     * Function for initial UI setup
     */
    private fun setupUI() {
        adapter = MainViewPagerAdapter(this)
        binding.viewPager.adapter = adapter
        setupTabLayoutMediator()
    }

    /**
     * Setup of {@link TabLayoutMediator}.
     * It's needed for setting up tabs text.
     */
    private fun setupTabLayoutMediator() {
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    /**
     * Click processing for list of users.
     *
     * @param id User ID.
     */
    fun processUserClick(id: Int) {
        setCurrentUserId(id)
        openAlbumsPage()
    }

    /**
     * Click processing for list of albums.
     *
     * @param id Album ID.
     */
    fun processAlbumClick(id: Int) {
        setCurrentAlbumId(id)
        openPhotosPage()
    }

    private fun openAlbumsPage() {
        binding.viewPager.setCurrentItem(TAB_ALBUM, true)
    }

    private fun openPhotosPage() {
        binding.viewPager.setCurrentItem(TAB_PHOTO, true)
    }

    fun getCurrentUserId() = currentUserId

    fun setCurrentUserId(id: Int) {
        currentUserId = id
    }

    fun getCurrentAlbumId() = currentAlbumId

    fun setCurrentAlbumId(id: Int) {
        currentAlbumId = id
    }
}