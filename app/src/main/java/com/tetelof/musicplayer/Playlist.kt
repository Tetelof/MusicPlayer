package com.tetelof.musicplayer

import android.content.Context
import android.widget.Toast

class Playlist
{
    companion object {
        val playlist: MutableList<Music> = mutableListOf()


        fun nextMusic(atual: Music, context: Context) {
            try {
                Music.playContentUri(playlist[playlist.indexOf(atual) + 1], context)
            } catch (e: Exception) {
                if (Music.loop) Music.playContentUri(playlist[0], context)
                else Toast.makeText(context, "Fim da playlist" + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        fun previousMusic(atual: Music, context: Context) {
            try {
                Music.playContentUri(playlist[playlist.indexOf(atual) - 1], context)
            } catch (e: Exception) {
                if (Music.loop) Music.playContentUri(playlist.last(), context)
                else Music.playContentUri(atual, context)
            }
        }

        fun createPlaylist(musicFiles: MutableList<Music>) {
            playlist.clear()
            playlist.addAll(musicFiles)
        }
        fun createFromMusic(music: Music, musicFiles: MutableList<Music>){
            val index = musicFiles.indexOf(music)
            for(i in index until musicFiles.size-1){
                playlist.add(musicFiles[i])
            }
            for(j in 0..index){
                playlist.add(musicFiles[j])
            }
        }

        fun startPlaylist(context: Context) {
            Music.playContentUri(playlist[0], context)
        }

        fun clearPlaylist() {
            this.playlist.clear()
        }

        fun addMusic(music: Music) {
            playlist.add(music)
        }

        fun addMusicNext(atual: Music, next: Music) {
            playlist.add(playlist.indexOf(atual),next)
        }

        fun removeMusic(music: Music) {
            playlist.remove(music)
        }
    }
}