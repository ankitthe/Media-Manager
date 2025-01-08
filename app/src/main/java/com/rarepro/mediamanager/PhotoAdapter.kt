package com.rarepro.mediamanager

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PhotoAdapter(private val context: Context, private val photoList: List<String>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photoList[position]

        // Load the photo using Glide
        Glide.with(context)
            .load(photo) // Assuming photoList contains paths to the images
            .into(holder.photoImageView)

        // Set click listener on the image
        holder.photoImageView.setOnClickListener {
            val intent = Intent(context, PhotoViewerActivity::class.java)
            intent.putStringArrayListExtra("photo_list", ArrayList(photoList))
            intent.putExtra("initial_position", position)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = photoList.size

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.photo_image_view)
    }
}