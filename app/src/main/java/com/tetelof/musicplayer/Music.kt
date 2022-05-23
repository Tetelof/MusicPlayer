package com.tetelof.musicplayer

import android.content.Context
import android.graphics.Bitmap
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import java.io.IOException

class Music {
    var id: Int = 0
    var title: String
    var artist: String
    var path: Uri
    var cover: Bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)

    constructor(id: Int, title: String, artist: String, path: Uri, cover: Bitmap){
        this.id = id
        this.title = title
        this.artist = artist
        this.path = path
        this.cover = cover
    }

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
                    Toast.makeText(context, "Abrindo musica", Toast.LENGTH_SHORT).show()
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
    }
}
