package com.tetelof.musicplayer

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.os.Handler as Handler

class Player : AppCompatActivity() {

    lateinit var runnable: Runnable
    private var handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // timers da musica
        val tempoAtual: TextView = findViewById(R.id.playerTempoAtual)
        val duração: TextView = findViewById(R.id.playerDuracao)


        // define tamanho da seekbar e o timer de duração
        val seekbar: SeekBar = findViewById(R.id.playerSeekBar)
        seekbar.progress = 0
        if(Music.mediaPlayer!=null) {
            seekbar.max = Music.mediaPlayer!!.duration
            duração.text = millToMinutes(Music.mediaPlayer!!.duration)
        }
        else seekbar.max = 1


        // atualiza os nomes de artista e da musica e atualiza a imagem de cover
        val musicTile = findViewById<TextView>(R.id.playerMusicTitle)
        val musicArtist = findViewById<TextView>(R.id.playerMusicArtist)
        val cover = findViewById<ImageView>(R.id.playerCover)
        musicTile.text = Music.currentPlaying!!.title
        musicArtist.text = Music.currentPlaying!!.artist
        if (Music.currentPlaying!!.cover != null) cover.setImageBitmap(Music.currentPlaying!!.cover)


        // botao de play/pause
        val playPauseButton = findViewById<ImageButton>(R.id.playerPlayPauseButton)
        if(Music.mediaPlayer != null && Music.mediaPlayer!!.isPlaying) {
            playPauseButton.setImageResource(R.drawable.ic_baseline_pause_circle_outline_50)
        }else{
            playPauseButton.setImageResource(R.drawable.ic_baseline_play_circle_outline_50)
        }

        // listeners do play/pause
        playPauseButton.setOnClickListener{
              if(Music.mediaPlayer != null && !Music.mediaPlayer!!.isPlaying){
                  Music.resumeSound()
                  playPauseButton.setImageResource(R.drawable.ic_baseline_pause_circle_outline_50)
              }else if(Music.mediaPlayer != null && Music.mediaPlayer!!.isPlaying)
              {
                  Music.pauseSound()
                  playPauseButton.setImageResource(R.drawable.ic_baseline_play_circle_outline_50)
              }else{
                  Toast.makeText(this, "Nenhuma musica carregada no player.", Toast.LENGTH_SHORT).show()
              }
        }


        // movimentação do seekbar
        seekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, changed: Boolean) {
                if(changed && Music.mediaPlayer != null){
                    Music.mediaPlayer!!.seekTo(p1)
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
        runnable = Runnable {
            if(Music.mediaPlayer != null) {
                seekbar.progress = Music.mediaPlayer!!.currentPosition
                tempoAtual.text = millToMinutes(Music.mediaPlayer!!.currentPosition)
            }
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
        if(Music.mediaPlayer != null)Music.mediaPlayer!!.setOnCompletionListener {
            playPauseButton.setImageResource(R.drawable.ic_baseline_play_circle_outline_50)
            seekbar.progress = 0
            Playlist.nextMusic(Music.currentPlaying!!,)
        }
    }

    private fun millToMinutes(milliseconds: Int): String{
        val minutes = milliseconds / 1000 / 60
        val seconds = milliseconds / 1000 % 60
        return String.format("%02d:%02d",minutes,seconds)
    }


}