@file:Suppress("DEPRECATION")

package com.rarepro.mediamanager

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class VideoAdapter(private val videoList: List<VideoModel>, private val context: Context) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private val videoDurations = mutableMapOf<String, String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]

        // Load video thumbnail using Glide
        Glide.with(holder.itemView.context)
            .load(video.path) // Path to the video file
            .error(R.drawable.video_placeholder) // Default placeholder image
            .into(holder.videoThumbnail)

        // Set video duration
        val cachedDuration = videoDurations[video.path]
        if (cachedDuration != null) {
            holder.videoDuration.text = cachedDuration
        } else {
            // If the duration is not cached, load it asynchronously
            loadVideoDurationAsync(holder, video.path)
        }

        // Set onClickListener to open VideoPlayerActivity on item click
        holder.itemView.setOnClickListener {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putStringArrayListExtra("VIDEO_LIST", ArrayList(videoList.map { it.path }))
            intent.putExtra("CURRENT_POSITION", position)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = videoList.size


    private fun loadVideoDurationAsync(holder: VideoViewHolder, videoPath: String) {
        // Perform the duration fetching task on a background thread
        FetchVideoDurationTask(holder, videoPath).execute()
    }

    private inner class FetchVideoDurationTask(
        val holder: VideoViewHolder,
        val videoPath: String
    ) : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void?): String {
            return getVideoDuration(videoPath)
        }

        override fun onPostExecute(result: String) {
            // Once the duration is fetched, set it on the UI and cache it
            videoDurations[videoPath] = result
            holder.videoDuration.text = result
        }
    }

    private fun getVideoDuration(videoPath: String): String {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(videoPath)
            val durationMillis = retriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_DURATION
            )?.toLongOrNull() ?: 0L

            // Format milliseconds into "MM:SS"
            String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(durationMillis),

                TimeUnit.MILLISECONDS.toSeconds(durationMillis) % 60
            )
        } catch (e: Exception) {
            Log.e("VideoAdapter", "Error fetching duration: ${e.message}")
            "00:00"
        } finally {
            retriever.release()
        }
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoThumbnail: ImageView = itemView.findViewById(R.id.video_thumbnail)
        val videoDuration: TextView = itemView.findViewById(R.id.video_duration)
    }
}


