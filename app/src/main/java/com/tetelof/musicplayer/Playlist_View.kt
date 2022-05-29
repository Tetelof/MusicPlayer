package com.tetelof.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Playlist_View : AppCompatActivity() {

    private lateinit var playlistRecyclerView: RecyclerView
    private lateinit var adapter: MusicAdapter
    var x1 = 0f
    var x2 = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_view)

        adapter = MusicAdapter(this, Playlist.playlist)

        playlistRecyclerView = findViewById(R.id.playlistRecyclerView)
        playlistRecyclerView.layoutManager = LinearLayoutManager(this)
        playlistRecyclerView.adapter = adapter
    }
    override fun onTouchEvent(touchEvent: MotionEvent): Boolean{
        var distance = 0f
        when (touchEvent.action){
            MotionEvent.ACTION_DOWN ->
                x1 = touchEvent.x
            MotionEvent.ACTION_UP ->
                x2 = touchEvent.x
        }
        distance = x2 - x1
        if (distance > 50){
            val intent = Intent(this, Playlist_View::class.java)
            startActivity(intent)
        }
        return false
    }
}