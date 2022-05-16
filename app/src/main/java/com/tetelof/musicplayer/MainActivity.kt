package com.tetelof.musicplayer

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.media.AudioAttributes
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
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

    fun getAllAudioFromDevice(context: Context){
        val resolver: ContentResolver = contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor? = resolver.query(uri, null, null, null, null)
        when {
            cursor == null -> {
                Toast.makeText(this, "deu ruim", Toast.LENGTH_SHORT).show()
            }
            !cursor.moveToFirst() -> {
                Toast.makeText(this, "Sem mídia no dispositivo", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val titleColumn: Int = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val idColumn: Int = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                do {
                    val thisId = cursor.getLong(idColumn)
                    val thisTitle = cursor.getString(titleColumn)
                    mediaPlayerSet(thisId)
                } while (cursor.moveToNext())
            }
        }
        cursor?.close()
    }
    fun mediaPlayerSet(id : Long){
        val contentUri : Uri = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id)
        val mediaPlayer = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDataSource(applicationContext, contentUri)
        }
    }
}