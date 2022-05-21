package com.tetelof.musicplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException

class MusicAdapter(private val context: Context, private val musicList: MutableList<Music>) :
    RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    private var mMediaPlayer: MediaPlayer? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.music_layout, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musicList[position]
        holder.musicName.text = music.title
        holder.musicAuthor.text = music.artist


        holder.itemView.setOnClickListener{
            Music.playContentUri(music.path, context)
        }
        holder.stopButton.setOnClickListener{
            Music.stopSound()
        }
    }

    override fun getItemCount(): Int {return musicList.size}

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val musicName: TextView = itemView.findViewById(R.id.musicName)
        val musicAuthor: TextView = itemView.findViewById(R.id.musicAuthor)
        val stopButton: ImageView = itemView.findViewById(R.id.stopButton)
    }
}