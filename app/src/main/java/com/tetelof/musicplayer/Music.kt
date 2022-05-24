package com.tetelof.musicplayer

import android.content.Context
import android.graphics.Bitmap
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import java.io.IOException

class Music(var title: String, var artist: String, var path: Uri, var cover: Bitmap) {

    companion object MusicPlayer{
        var mediaPlayer: MediaPlayer? = null
        var currentPlaying: Music? = null
        var loop: Boolean = true



        fun playContentUri(music : Music, context: Context){
            this.currentPlaying = music



            if (mediaPlayer != null && mediaPlayer!!.isPlaying){
                stopSound()
            }
            try {

                mediaPlayer = MediaPlayer().apply{
                    setDataSource(context, currentPlaying!!.path)
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    prepareAsync()
                    start()
                }
                mediaPlayer!!.setOnPreparedListener{
                    mediaPlayer!!.start()
                    atualizaInfo(music, context)
//                    Toast.makeText(context, "Abrindo musica", Toast.LENGTH_SHORT).show()
                }

            }catch (e : IOException){
                mediaPlayer?.release()
                mediaPlayer = null
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }catch (e: Exception){
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        fun stopSound() {
            if (mediaPlayer != null) {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = null
            }
        }
        fun pauseSound(){
            if (mediaPlayer != null && mediaPlayer!!.isPlaying){
                mediaPlayer!!.pause()
            }
        }
        fun resumeSound(){
            if (mediaPlayer != null && !mediaPlayer!!.isPlaying){
                mediaPlayer!!.start()
            }
        }
        fun atualizaInfo(music: Music, context: Context){
            try {
                context as MainActivity
                context.musicArtist.text = music.artist
                context.musicTitle.text = music.title
                context.musicImage.setImageBitmap(music.cover)
                context.playPauseButton.setImageResource(R.drawable.ic_baseline_pause_circle_outline_50)
            }catch (e: Exception){}
            try {
                context as Player
                context.musicArtist.text = music.artist
                context.musicTitle.text = music.title
                context.musicImage.setImageBitmap(music.cover)
                context.playPauseButton.setImageResource(R.drawable.ic_baseline_pause_circle_outline_50)
                context.seekbar.max = mediaPlayer!!.duration
                context.duracao.text = context.millToMinutes(mediaPlayer!!.duration)
            }catch (e: Exception) {}
        }
    }
}
