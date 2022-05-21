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
    var title: String = ""
    var artist: String = ""
    var path: Uri = Uri.EMPTY
    var cover: Bitmap = Bitmap.createBitmap(R.drawable.ic_baseline_pause_circle_outline_50)

    constructor(id: Int, title: String, artist: String, path: Uri, audioCover:Bitmap){
        this.id = id
        this.title = title
        this.artist = artist
        this.path = path
    }
    companion object MusicPlayer{
        var mediaPlayer: MediaPlayer? = null

        fun playContentUri(path : Uri, context: Context){
            if (mediaPlayer != null && mediaPlayer!!.isPlaying){
                stopSound()
            }
            try {

                mediaPlayer = MediaPlayer().apply{
                    setDataSource(context, path)
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    prepareAsync()
                    start()
                }
                Toast.makeText(context, "Abrindo musica", Toast.LENGTH_SHORT).show()
            }catch (e : IOException){
                mediaPlayer?.release()
                mediaPlayer = null
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }catch (e: Exception){
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        fun stopSound(){
            if(mediaPlayer != null){
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = null
            }
        }

    }
}
