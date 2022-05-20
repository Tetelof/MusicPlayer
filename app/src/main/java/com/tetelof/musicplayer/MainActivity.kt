package com.tetelof.musicplayer

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {


    private var mMediaPlayer: MediaPlayer? = null
    private lateinit var musicRecyclerView: RecyclerView
    private lateinit var musicList: MutableList<Music>
    private lateinit var adapter: MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!havePermission()) {
            requestForSpecificPermission()
        }
        musicList = mutableListOf()
        musicList = musicFiles()
        adapter = MusicAdapter(this, musicList)

        musicRecyclerView = findViewById(R.id.musicRecyclerView)
        musicRecyclerView.layoutManager = LinearLayoutManager(this)
        musicRecyclerView.adapter = adapter

    }
    private fun musicFiles():MutableList<Music>{
        // Initialize an empty mutable list of music
        val list:MutableList<Music> = mutableListOf()

        // Get the external storage media store audio uri
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        //val uri: Uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI

        // IS_MUSIC : Non-zero if the audio file is music
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"

        // Sort the musics
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        //val sortOrder = MediaStore.Audio.Media.TITLE + " DESC"

        // Query the external storage for music files
        val cursor: Cursor? = this.contentResolver.query(
            uri, // Uri
            null, // Projection
            selection, // Selection
            null, // Selection arguments
            sortOrder // Sort order
        )

        // If query result is not empty
        if (cursor!= null && cursor.moveToFirst()){
            val id:Int = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val title:Int = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artist: Int = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)

            // Now loop through the music files
            do {
                val audioId:Long = cursor.getLong(id)
                val audioTitle:String = cursor.getString(title)
                val audioPath: Uri = Uri
                    .withAppendedPath(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        "" + cursor.getLong(id)
                    )
                val audioArtist: String = cursor.getString(artist)

                // Add the current music to the list
                list.add(Music(audioId,audioTitle,audioArtist,audioPath))
            }while (cursor.moveToNext())
        }

        // Finally, return the music files list
        cursor?.close()
        return  list
    }
    override fun onStop() {
        super.onStop()
        if(mMediaPlayer != null){
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }
    private fun havePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)
        return result == PackageManager.PERMISSION_GRANTED
    }
    private fun requestForSpecificPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            101
        )
    }
}