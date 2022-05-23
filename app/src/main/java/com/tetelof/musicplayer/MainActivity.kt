package com.tetelof.musicplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(){


    private var mMediaPlayer: MediaPlayer? = null
    private lateinit var musicRecyclerView: RecyclerView
    private lateinit var musicList: MutableList<Music>
    private lateinit var adapter: MusicAdapter
    lateinit var musicImage: ImageView
    lateinit var playPauseButton: ImageButton
    private lateinit var stopButton: ImageButton
    lateinit var musicTitle: TextView
    lateinit var musicArtist: TextView
    private lateinit var player: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!havePermission()) {
            requestForSpecificPermission()
        }
        musicList = mutableListOf()
        musicList = musicFiles()
        Playlist.createPlaylist(musicList)
        adapter = MusicAdapter(this, musicList)

        musicRecyclerView = findViewById(R.id.musicRecyclerView)
        musicRecyclerView.layoutManager = LinearLayoutManager(this)
        musicRecyclerView.adapter = adapter

        musicImage = findViewById(R.id.mainAlbumCover)
        musicTitle = findViewById(R.id.mainMusicName)
        musicArtist = findViewById(R.id.mainMusicAuthor)
        playPauseButton = findViewById(R.id.mainPlayPauseButton)
        stopButton = findViewById(R.id.mainStopButton)
        player = findViewById(R.id.player)

        player.setOnClickListener{
            val intent = Intent(this, Player::class.java)
            startActivity(intent)
        }
        stopButton.setOnClickListener{
            Music.stopSound()
            playPauseButton.setImageResource(R.drawable.ic_baseline_play_circle_outline_50)
        }


        playPauseButton.setOnClickListener{
            if (Music.mediaPlayer != null && Music.mediaPlayer!!.isPlaying){
                Music.pauseSound()
                playPauseButton.setImageResource(R.drawable.ic_baseline_play_circle_outline_50)
            }else if(Music.mediaPlayer != null && !Music.mediaPlayer!!.isPlaying){
                Music.resumeSound()
                playPauseButton.setImageResource(R.drawable.ic_baseline_pause_circle_outline_50)
            }else{
                Playlist.startPlaylist(this)
                playPauseButton.setImageResource(R.drawable.ic_baseline_pause_circle_outline_50)
            }
        }



    }


    private fun musicFiles():MutableList<Music>{
        val list:MutableList<Music> = mutableListOf()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"

        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"

        val cursor: Cursor? = this.contentResolver
            .query(uri, null, selection, null, sortOrder)
        if (cursor!= null && cursor.moveToFirst()){
            val id:Int = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val title:Int = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artist: Int = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            do {
                val audioId:Int = cursor.getInt(id)
                val audioTitle:String = cursor.getString(title)
                val audioPath: Uri = Uri
                    .withAppendedPath(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        "" + cursor.getLong(id)
                    )
                val audioArtist: String = cursor.getString(artist)
                val audioCover = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
                list.add(Music(audioId, audioTitle, audioArtist, audioPath, audioCover))
            }while (cursor.moveToNext())
        }
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