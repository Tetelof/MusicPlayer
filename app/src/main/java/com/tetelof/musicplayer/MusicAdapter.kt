package com.tetelof.musicplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tetelof.musicplayer.Music

class MusicAdapter(val context: Context, val musicList: MutableList<Music>) :
    RecyclerView.Adapter<UserAdapter.MusicViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.music_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val music = musicList[position]



        holder.musicName.text = music.title
        holder.musicAuthor.text = music.author

        holder.itemView.setOnClickListener{
            stopSound()
            playContentUri(music.path)
        }
    }

    override fun getItemCount(): Int {return userList.size}

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val musicName = itemView.findViewById<TextView>(R.id.musicName)
        val musicAuthor = itemView.findViewById<TextView>(R.id.musicAuthor)
    }
}