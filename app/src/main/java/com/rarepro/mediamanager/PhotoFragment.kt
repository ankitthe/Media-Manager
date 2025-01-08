package com.rarepro.mediamanager

import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PhotoFragment : Fragment() {

    private lateinit var photoRecyclerView: RecyclerView
    private val photoList = mutableListOf<String>() // List to store photo paths

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo, container, false)

        // Initialize RecyclerView
        photoRecyclerView = view.findViewById(R.id.photo_recycler_view)

        // Set GridLayoutManager with 4 columns
        photoRecyclerView.layoutManager = GridLayoutManager(requireContext(), 4)

        // Load photo files
        loadPhotoFiles()

        // Set adapter
        val photoAdapter = PhotoAdapter(requireContext(), photoList)
        photoRecyclerView.adapter = photoAdapter

        return view
    }

    private fun loadPhotoFiles() {
        val projection = arrayOf(MediaStore.Images.Media.DATA) // To get file path
        val cursor = requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (it.moveToNext()) {
                val filePath = it.getString(dataColumn)
                photoList.add(filePath) // Add photo path to list
            }
        }

        // Reverse the list to show new images first
        photoList.reverse()

        // Notify adapter that data has changed
        photoRecyclerView.adapter?.notifyDataSetChanged()
    }
}