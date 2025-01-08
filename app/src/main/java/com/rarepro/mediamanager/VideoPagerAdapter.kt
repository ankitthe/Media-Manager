package com.rarepro.mediamanager

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView

class VideoPagerAdapter(
    private val context: Context,
    private val videoList: List<String>
) : RecyclerView.Adapter<VideoPagerAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_video_player, parent, false)
        return VideoViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: VideoViewHolder,  position: Int) {
        val videoPath = videoList[position]
        val uri = Uri.parse(videoPath)

        // Set up the VideoView
        holder.videoView.setVideoURI(uri)
        holder.videoView.setMediaController(MediaController(context))
        holder.videoView.setOnPreparedListener {
            it.start() // Start video playback automatically
        }
    }




    override fun getItemCount(): Int = videoList.size

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoView: VideoView = itemView.findViewById(R.id.video_view)
    }
}
