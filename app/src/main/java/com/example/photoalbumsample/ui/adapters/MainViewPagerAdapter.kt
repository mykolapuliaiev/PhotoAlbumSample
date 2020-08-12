package com.example.photoalbumsample.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.photoalbumsample.ui.content.ContentViewPagerFragment

class MainViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment =
        ContentViewPagerFragment.newInstance(position)
}