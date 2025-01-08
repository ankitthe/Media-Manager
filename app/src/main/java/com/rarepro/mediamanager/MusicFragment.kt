package com.rarepro.mediamanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MusicFragment : Fragment() {

    private lateinit var musicRecyclerView: RecyclerView
    private val musicList = mutableListOf<String>()
    private var mediaPlayer: MediaPlayer? = null
    private var currentPlayingPosition = -1 // Track currently playing song position

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_music, container, false)

        musicRecyclerView = view.findViewById(R.id.music_recycler_view)
        musicRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Check and request permissions
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadMusicFiles()
        } else {
            requestStoragePermission()
        }

        return view
    }

    private fun loadMusicFiles() {
        val projection = arrayOf(MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA)
        val cursor = requireContext().contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                val title = it.getString(titleColumn)
                val data = it.getString(dataColumn)
                musicList.add(data) // Save the file path
            }
        }

        // Set up RecyclerView Adapter
        musicRecyclerView.adapter = MusicAdapter(musicList, currentPlayingPosition) { position ->
            handleMusicPlayback(position)
        }
    }

    private fun handleMusicPlayback(position: Int) {
        // Open MusicPlayerActivity with the song title and path
        val intent = Intent(requireContext(), MusicPlayer::class.java).apply {
            putExtra("song_title", musicList[position].substringAfterLast("/"))  // Song title
            putExtra("song_path", musicList[position])  // Song path
        }
        startActivity(intent)  // Start MusicPlayerActivity
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            100
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadMusicFiles()
        } else {
            Toast.makeText(requireContext(), "Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Release media player when fragment is destroyed
    }
}
