package com.rarepro.mediamanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class PhotoViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_viewer)

        val photoViewPager: ViewPager2 = findViewById(R.id.photo_view_pager)

        // Retrieve the photo list and the initial position
        val photoList = intent.getStringArrayListExtra("photo_list") ?: arrayListOf()
        val initialPosition = intent.getIntExtra("initial_position", 0)

        // Set up ViewPager2 with the adapter
        val adapter = PhotoPagerAdapter(this, photoList)
        photoViewPager.adapter = adapter

        // Set the current item to the initial position
        photoViewPager.setCurrentItem(initialPosition, false)
    }
}
