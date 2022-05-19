package com.tetelof.musicplayer

import android.content.ContentUris
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable: Runnable
    private var handler = Handler()
    private var pause: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION

        val button = findViewById<Button>(R.id.button)
        val listView = findViewById<ListView>(R.id.list_view)


        button.setOnClickListener{
            
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
            val artist: String = cursor.getColumnIndex(MediaStore.Audio.ARTIST)
            val path = ContentUris
                .withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getLong(id))

            // Now loop through the music files
            do {
                val audioId:Long = cursor.getLong(id)
                val audioTitle:String = cursor.getString(title)
                val audioPath: Uri = path
                val audioArtist: String = artist

                // Add the current music to the list
                list.add(Music(audioId,audioTitle,audioArtist,audioPath))
            }while (cursor.moveToNext())
        }

        // Finally, return the music files list
        cursor?.close()
        return  list
    }
    public fun playContentUri(path : String){
        mMediaPlayer = null
        try {

            mMediaPlayer = MediaPlayer().apply{
                setDataSource(path)
                setAudioAttributes(AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
                )
                prepare()
                start()
            }
            Toast.makeText(this, "Abrindo musica", Toast.LENGTH_SHORT).show()
        }catch (e : IOException){
            mMediaPlayer?.release()
            mMediaPlayer = null
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
    override fun onStop() {
        super.onStop()
        if(mMediaPlayer != null){
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }
    fun stopSound(){
        if(mMediaPlayer != null){
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }
}