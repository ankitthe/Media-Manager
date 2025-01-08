package com.rarepro.mediamanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class VideoPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_pager)

        // Get video list and initial position
        val videoList = intent.getStringArrayListExtra("VIDEO_LIST") ?: arrayListOf()
        val initialPosition = intent.getIntExtra("CURRENT_POSITION", 0)

        // Set up ViewPager2
        val viewPager: ViewPager2 = findViewById(R.id.video_view_pager)
        val adapter = VideoPagerAdapter(this, videoList)
        viewPager.adapter = adapter

        // Set the initial position
        viewPager.setCurrentItem(initialPosition, false)
    }
}
