package com.rarepro.mediamanager

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MusicAdapter(
    private val musicList: List<String>,
    private var currentPlayingPosition: Int,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val musicTitle = musicList[position]
        holder.musicTitle.text = musicTitle

        // Click listener to play or stop the music
        holder.playStopImageView.setOnClickListener {
            onItemClick(position)
            Log.d("Monit", "$position" )
        }


        // Dynamically update ImageView based on media player state
        if (position == currentPlayingPosition) {
            holder.playStopImageView.setImageResource(R.drawable.pause1)  // Pause icon
        } else {
            holder.playStopImageView.setImageResource(R.drawable.play1)  // Play icon
        }

        // Handle Play/Stop icon change
        holder.playStopImageView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int = musicList.size

    // Update the current playing position and notify the adapter
    fun updatePlayingPosition(newPosition: Int) {
        currentPlayingPosition = newPosition
        notifyDataSetChanged()
    }

    inner class MusicViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val musicImageView: ImageView = itemView.findViewById(R.id.music_image_view)
        val musicTitle: TextView = itemView.findViewById(R.id.music_title)
        val playStopImageView: ImageView = itemView.findViewById(R.id.play_stop_image_view)
    }
}
