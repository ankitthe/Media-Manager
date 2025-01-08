package com.rarepro.mediamanager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(activity: MainActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 3  // Total number of fragments (Music, Videos, Photos)
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MusicFragment()
            1 -> VideoFragment()
            2 -> PhotoFragment()
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }
}
