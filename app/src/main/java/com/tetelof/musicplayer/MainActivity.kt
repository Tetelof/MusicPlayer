package com.tetelof.musicplayer

import android.content.ContentUris
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

var mMediaPlayer: MediaPlayer? = null

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val button = findViewById<Button>(R.id.button)
        val listView = findViewById<ListView>(R.id.list_view)

        // Button click listener
        button.setOnClickListener {
            // Get the external storage/sd card music files list
            val list: MutableList<Music> = musicFiles()

            // Get the sd card music titles list
            val titles = mutableListOf<String>()
            for (music in list) {
                titles.add(music.title)
            }

            // Display external storage music files list on list view
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, titles)
            listView.adapter = adapter
        }
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
            val path = ContentUris
                .withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getLong(id))

            // Now loop through the music files
            do {
                val audioId:Long = cursor.getLong(id)
                val audioTitle:String = cursor.getString(title)
                val audioPath: Uri = path

                // Add the current music to the list
                list.add(Music(audioId,audioTitle,audioPath))
            }while (cursor.moveToNext())
        }

        // Finally, return the music files list
        cursor?.close()
        return  list
    }
}