package com.tetelof.musicplayer

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MusicAdapter(private val context: MainActivity, private val musicList: MutableList<Music>) :
    RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.music_layout, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musicList[position]
        holder.musicName.text = music.title
        holder.musicAuthor.text = music.artist
        changeCover(music)
        holder.miniAlbumCover.setImageBitmap(music.cover)



        holder.itemView.setOnClickListener{
            Music.playContentUri(music, context)
            context.playPauseButton.setImageResource(R.drawable.ic_baseline_pause_circle_outline_50)
        }
    }

    override fun getItemCount(): Int {return musicList.size}

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val musicName: TextView = itemView.findViewById(R.id.musicName)
        val musicAuthor: TextView = itemView.findViewById(R.id.musicAuthor)
        val miniAlbumCover: ImageView = itemView.findViewById(R.id.miniAlbumCover)
    }

    fun changeCover(music: Music){
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(context,music.path)
        val data = mmr.embeddedPicture

        if(data != null){
            music.cover = BitmapFactory.decodeByteArray(data, 0, data.size)
        }

    }
}