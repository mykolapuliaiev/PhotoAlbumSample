package com.example.photoalbumsample.ui.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photoalbumsample.R
import com.example.photoalbumsample.databinding.FragmentMainBinding
import com.example.photoalbumsample.models.ContentModel
import com.example.photoalbumsample.ui.adapters.ContentAdapter
import com.example.photoalbumsample.ui.main.MainActivity
import com.example.photoalbumsample.utils.Constants.Companion.TAB_ALBUM
import com.example.photoalbumsample.utils.Constants.Companion.TAB_PHOTO
import com.example.photoalbumsample.utils.Constants.Companion.TAB_USER
import com.example.photoalbumsample.utils.CustomProgressBar
import com.example.photoalbumsample.utils.GridItemDecorator
import com.example.photoalbumsample.utils.onItemClick

class ContentViewPagerFragment : Fragment() {

    companion object {
        private const val TAB_INDEX = "tab_index"

        /**
         * Function for fragment instantiation.
         *
         * @param position Position of current fragment inside of ViewPager. Will be passed into the fragment.
         * @return Fragment instance
         */
        fun newInstance(position: Int): ContentViewPagerFragment {
            val fragment = ContentViewPagerFragment()
            val bundle = Bundle().apply {
                putInt(TAB_INDEX, position)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    private val viewModel: ContentViewModel by lazy {
        ViewModelProvider(this).get(ContentViewModel::class.java)
    }

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: ContentAdapter
    private lateinit var layoutManager: LinearLayoutManager

    /**
     * Lifecycle functions
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //DataBinding setup
        val layout = R.layout.fragment_main
        binding = DataBindingUtil.inflate(inflater, layout, container, false)

        processArguments()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUI()
        setupObservers()

        when (viewModel.currentTab) {
            TAB_USER -> viewModel.getUsers()
            TAB_ALBUM -> {
                viewModel.currentUserID = (activity as? MainActivity)?.getCurrentUserId() ?: 1
                viewModel.getAlbumsByID(viewModel.currentUserID)
            }
            TAB_PHOTO -> {
                binding.footerLayout.visibility = View.VISIBLE
                viewModel.currentAlbumID = (activity as? MainActivity)?.getCurrentAlbumId() ?: 1
                viewModel.getPhotosByID(viewModel.currentAlbumID)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (viewModel.currentTab == TAB_ALBUM) {
            val globalUserId = (activity as? MainActivity)?.getCurrentUserId() ?: 1
            if (viewModel.currentUserID != globalUserId) {
                viewModel.currentUserID = globalUserId
                viewModel.getAlbumsByID(viewModel.currentUserID)
            }
        } else if (viewModel.currentTab == TAB_PHOTO) {
            viewModel.currentPhotosChunk = 0
            val globalAlbumId = (activity as? MainActivity)?.getCurrentAlbumId() ?: 1
            if (viewModel.currentAlbumID != globalAlbumId) {
                viewModel.currentAlbumID = globalAlbumId
                viewModel.getPhotosByID(viewModel.currentAlbumID)
            }
        }
    }

    /**
     * Flow setup
     */
    private fun setupUI() {
        binding.rvContent.onItemClick {
            when (val model = adapter.getItem(it)) {
                is ContentModel.User -> processUserClick(model)
                is ContentModel.Album -> processAlbumClick(model)
            }
        }

        binding.buttonLeft.setOnClickListener {
            if (viewModel.currentPhotosChunk != 0) {
                viewModel.currentPhotosChunk--
                setChunk()
                setCountText()
            }
        }

        binding.buttonRight.setOnClickListener {
            if (viewModel.currentPhotosChunk != viewModel.photosChunked.size - 1) {
                viewModel.currentPhotosChunk++
                setChunk()
                setCountText()
            }
        }
    }

    private fun setupObservers() {
        viewModel.usersLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                (activity as? MainActivity)?.setCurrentUserId(it.first().id)
                setupAdapter(it)
            }
        }

        viewModel.albumsLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                (activity as? MainActivity)?.setCurrentAlbumId(it.first().id)
                setupAdapter(it)
            }
        }

        viewModel.photosLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                viewModel.photosChunked = it.chunked(24)
                if (!this::adapter.isInitialized) {
                    setupAdapter(it)
                }
                setChunk()
                setCountText()
            }
        }

        viewModel.showProgress.observe(viewLifecycleOwner) {
            handleProgressObservable(it)
        }
    }

    private fun setupAdapter(items: List<ContentModel>) {
        adapter = ContentAdapter().apply {
            if (viewModel.currentTab != TAB_PHOTO) {
                setItems(items.toMutableList())
            }
        }

        layoutManager = if (viewModel.currentTab == TAB_PHOTO) {
            if (binding.rvContent.itemDecorationCount == 0) {
                binding.rvContent.addItemDecoration(GridItemDecorator())
            }
            GridLayoutManager(context, 3)
        } else {
            if (binding.rvContent.itemDecorationCount == 0) {
                binding.rvContent.addItemDecoration(
                    DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                )
            }
            LinearLayoutManager(context)
        }
        binding.rvContent.layoutManager = layoutManager
        binding.rvContent.adapter = adapter
    }

    /**
     * Parse arguments out of bundle.
     */
    private fun processArguments() {
        arguments?.let {
            viewModel.currentTab = it.getInt(TAB_INDEX, 0)
        }
    }

    private fun processUserClick(user: ContentModel.User) {
        (activity as? MainActivity)?.processUserClick(user.id)
    }

    private fun processAlbumClick(album: ContentModel.Album) {
        (activity as? MainActivity)?.processAlbumClick(album.id)
    }

    private fun showProgressBar() {
        CustomProgressBar.getInstance().showProgressBar(binding.rootLayout)
    }

    private fun hideProgressBar() {
        CustomProgressBar.getInstance().hideProgressBar()
    }

    private fun handleProgressObservable(progress: Boolean) {
        when (progress) {
            true -> showProgressBar()
            false -> hideProgressBar()
        }
    }

    private fun setChunk() {
        adapter.setItems(viewModel.photosChunked[viewModel.currentPhotosChunk].toMutableList())
    }

    private fun setCountText() {
        // 1. Calculate total count of photos
        var total = 0
        viewModel.photosChunked.forEach {
            total += it.size
        }

        var currentStart = 0
        var currentEnd = 0
        // 2. Find last visible photo index
        for (i in 0..viewModel.currentPhotosChunk) {
            currentEnd += viewModel.photosChunked[i].size
        }
        // 3. Find first visible photo index
        if (viewModel.currentPhotosChunk != 0) {
            for (i in 0 until viewModel.currentPhotosChunk) {
                currentStart += viewModel.photosChunked[i].size
            }
            currentStart += 1
        } else {
            currentStart = 1
        }

        // 4. Set text to footer
        val text = "$currentStart to $currentEnd of $total"
        binding.itemsCountText.text = text
    }
}