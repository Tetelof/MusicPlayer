package com.tetelof.musicplayer

import android.media.AudioAttributes
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaPlayer
import android.widget.ImageButton
import android.widget.Toast
import java.io.File
import java.io.IOException

var mMediaPlayer: MediaPlayer? = null

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playButton = findViewById<ImageButton>(R.id.playButton)
        val pauseButton = findViewById<ImageButton>(R.id.pauseButton)


        val file =File("/storage/emulated/0/Musicas/TONY IGY - Astronomia.mp3")

        playButton.setOnClickListener{
            playContentUri(file.absolutePath)
        }
        pauseButton.setOnClickListener{
            pauseSound()
        }
    }

//    fun playSound(){
//        if (mMediaPlayer == null){
//            mMediaPlayer = MediaPlayer.create(this,R.raw.water)
//            mMediaPlayer!!.isLooping = true
//            mMediaPlayer!!.start()
//        }else mMediaPlayer!!.start()
//    }

    private fun pauseSound(){
        if (mMediaPlayer?.isPlaying == true) mMediaPlayer?.pause()
    }

//    fun stopSound(){
//        if(mMediaPlayer != null){
//            mMediaPlayer!!.stop()
//            mMediaPlayer!!.release()
//            mMediaPlayer = null
//        }
//    }

    override fun onStop() {
        super.onStop()
        if(mMediaPlayer != null){
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    private fun playContentUri(path : String){
        mMediaPlayer = null
        try {

            mMediaPlayer = MediaPlayer().apply{
                setDataSource(path)
                setAudioAttributes(AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
                )
                prepare()
                start()
            }
            Toast.makeText(this, "Abrindo musica", Toast.LENGTH_SHORT).show()
        }catch (e : IOException){
            mMediaPlayer?.release()
            mMediaPlayer = null
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}