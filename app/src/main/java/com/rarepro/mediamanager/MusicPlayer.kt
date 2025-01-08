package com.rarepro.mediamanager

import android.content.ContentUris
import android.graphics.Bitmap
import android.provider.MediaStore
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MusicPlayer : AppCompatActivity() {

    private lateinit var songTitleTextView: TextView
    private lateinit var songThumbnailImageView: ImageView
    private lateinit var playPauseButton: ImageView
    private lateinit var previousButton: ImageView
    private lateinit var nextButton: ImageView

    private var mediaPlayer: MediaPlayer? = null
    private var songPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player) // Layout set karenge

        // Views ko initialize kar rahe hain
        songTitleTextView = findViewById(R.id.song_title)
        songThumbnailImageView = findViewById(R.id.song_thumbnail)
        playPauseButton = findViewById(R.id.btn_play_pause)
        previousButton = findViewById(R.id.btn_previous)
        nextButton = findViewById(R.id.btn_next)

        // Intent se song details fetch kar rahe hain
        val songTitle = intent.getStringExtra("song_title")
        songPath = intent.getStringExtra("song_path")

        // Title set kar rahe hain
        songTitleTextView.text = songTitle

        // MediaPlayer ko song ke path se initialize kar rahe hain
        songPath?.let {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(it)
                prepare()
            }

            // Thumbnail ko set karna
            val thumbnail = getAlbumThumbnail(it) // Thumbnail fetch karenge
            songThumbnailImageView.setImageBitmap(thumbnail ?: BitmapFactory.decodeResource(resources, R.drawable.music)) // Default thumbnail
        }

        // Play/Pause button ka click listener
        playPauseButton.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                playPauseButton.setImageResource(R.drawable.play1) // Play icon set karna
            } else {
                mediaPlayer?.start()
                playPauseButton.setImageResource(R.drawable.pause1) // Pause icon set karna
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Activity destroy hone par media player release karna
    }

    // Method to fetch album thumbnail from song path
    private fun getAlbumThumbnail(songPath: String): Bitmap? {
        val projection = arrayOf(MediaStore.Audio.Media.ALBUM_ID)
        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            MediaStore.Audio.Media.DATA + "=?",
            arrayOf(songPath),
            null
        )
        cursor?.use {
            if (it.moveToFirst()) {
                val albumId = it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                val albumUri = Uri.parse("content://media/external/audio/albumart")
                val uri = ContentUris.withAppendedId(albumUri, albumId)
                return try {
                    contentResolver.openFileDescriptor(uri, "r")?.let { pfd ->
                        BitmapFactory.decodeFileDescriptor(pfd.fileDescriptor)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }
        return null
    }
}
