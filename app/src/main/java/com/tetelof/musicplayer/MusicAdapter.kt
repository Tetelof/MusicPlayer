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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicAdapter(private val context: Context, private val musicList: MutableList<Music>) :
    RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.music_layout, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musicList[position]
        holder.musicName.text = music.title
        holder.musicAuthor.text = music.artist
        GlobalScope.launch {
            changeCover(music)
            withContext(Dispatchers.Main){
                holder.miniAlbumCover.setImageBitmap(music.cover)
            }
        }
        holder.itemView.setOnClickListener{
            Playlist.clearPlaylist()
            Playlist.createFromMusic(music,musicList)
            Playlist.startPlaylist(context)

        }
    }

    override fun getItemCount(): Int {return musicList.size}

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val musicName: TextView = itemView.findViewById(R.id.musicName)
        val musicAuthor: TextView = itemView.findViewById(R.id.musicAuthor)
        val miniAlbumCover: ImageView = itemView.findViewById(R.id.miniAlbumCover)
    }

    private fun changeCover(music: Music){
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(context,music.path)
        if (mmr.embeddedPicture != null){
            val data: ByteArray = mmr.embeddedPicture!!
            music.cover = BitmapFactory.decodeByteArray(data, 0, data.size)
        }else{
            music.cover = BitmapFactory.decodeResource(context.resources, R.drawable.cover)
        }
    }
}