package com.tetelof.musicplayer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(){


    private lateinit var musicRecyclerView: RecyclerView
    private lateinit var musicList: MutableList<Music>
    private lateinit var adapter: MusicAdapter
    lateinit var musicImage: ImageView
    lateinit var playPauseButton: ImageButton
    private lateinit var stopButton: ImageButton
    lateinit var musicTitle: TextView
    lateinit var musicArtist: TextView
    private lateinit var player: RelativeLayout
    lateinit var displayList: MutableList<Music>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!havePermission()) {
            requestForSpecificPermission()
        }
        musicList = mutableListOf()
        musicList = musicFiles()
        Playlist.createPlaylist(musicList)
        displayList = mutableListOf()
        displayList.addAll(musicList)
        adapter = MusicAdapter(this, displayList)

        musicRecyclerView = findViewById(R.id.musicRecyclerView)
        musicRecyclerView.layoutManager = LinearLayoutManager(this)
        musicRecyclerView.adapter = adapter

        musicImage = findViewById(R.id.mainAlbumCover)
        musicTitle = findViewById(R.id.mainMusicName)
        musicArtist = findViewById(R.id.mainMusicAuthor)
        playPauseButton = findViewById(R.id.mainPlayPauseButton)
        stopButton = findViewById(R.id.mainStopButton)
        player = findViewById(R.id.player)

        player.setOnClickListener {
            val intent = Intent(this, Player::class.java)
            startActivity(intent)
        }
        stopButton.setOnClickListener {
            Music.stopSound()
            playPauseButton.setImageResource(R.drawable.ic_baseline_play_circle_outline_50)
        }


        playPauseButton.setOnClickListener {
            if (Music.mediaPlayer != null && Music.mediaPlayer!!.isPlaying) {
                Music.pauseSound()
                playPauseButton.setImageResource(R.drawable.ic_baseline_play_circle_outline_50)
            } else if (Music.mediaPlayer != null && !Music.mediaPlayer!!.isPlaying) {
                Music.resumeSound()
                playPauseButton.setImageResource(R.drawable.ic_baseline_pause_circle_outline_50)
            } else {
                Playlist.startPlaylist(this)
                playPauseButton.setImageResource(R.drawable.ic_baseline_pause_circle_outline_50)
            }
        }


        val searchItem = findViewById<EditText>(R.id.searchBox)
        searchItem.addTextChangedListener {
            if (searchItem.text.toString().isNotEmpty()) {
                displayList.clear()
                val search = searchItem.text.toString().lowercase()
                musicList.forEach {
                    if (it.title.lowercase().contains(search) ||
                            it.artist.lowercase().contains(search)) {
                        displayList.add(it)
                    }
                }
                musicRecyclerView.adapter!!.notifyDataSetChanged()
            } else {
                displayList.clear()
                displayList.addAll(musicList)
                musicRecyclerView.adapter!!.notifyDataSetChanged()
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
                val audioTitle:String = cursor.getString(title)
                val audioPath: Uri = Uri
                    .withAppendedPath(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        "" + cursor.getLong(id)
                    )
                val audioArtist: String = cursor.getString(artist)
                val audioCover = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
                list.add(Music(audioTitle, audioArtist, audioPath, audioCover))
            }while (cursor.moveToNext())
        }
        cursor?.close()
        return  list
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


    private fun changeCover(music: Music){
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(this,music.path)
        if (mmr.embeddedPicture != null){
            val data: ByteArray = mmr.embeddedPicture!!
            music.cover = BitmapFactory.decodeByteArray(data, 0, data.size)
        }else{
            music.cover = BitmapFactory.decodeResource(this.resources, R.drawable.cover)
        }
    }

    override fun onResume() {
        super.onResume()
       if (Music.currentPlaying != null){
           Music.atualizaInfo(Music.currentPlaying!!,this)
           if (Music.mediaPlayer != null){
               if(Music.mediaPlayer!!.isPlaying)
                   this.playPauseButton.setImageResource(R.drawable.ic_baseline_pause_circle_outline_50)
               else
                   this.playPauseButton.setImageResource(R.drawable.ic_baseline_play_circle_outline_50)
           }
       }
    }
}