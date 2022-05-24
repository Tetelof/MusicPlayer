package com.tetelof.musicplayer

import android.os.Bundle
import android.os.Handler
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Player : AppCompatActivity() {
    lateinit var musicTitle: TextView
    lateinit var musicArtist: TextView
    lateinit var musicImage: ImageView
    lateinit var playPauseButton: ImageButton
    lateinit var nextButton: ImageButton
    lateinit var previousButton: ImageButton
    lateinit var seekbar: SeekBar
    lateinit var duracao: TextView

    private lateinit var runnable: Runnable
    private var handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // timers da musica
        val tempoAtual: TextView = findViewById(R.id.playerTempoAtual)
        duracao= findViewById(R.id.playerDuracao)


        // define tamanho da seekbar e o timer de duração
        seekbar= findViewById(R.id.playerSeekBar)
        seekbar.progress = 0
        if(Music.mediaPlayer!=null) {
            seekbar.max = Music.mediaPlayer!!.duration
            duracao.text = millToMinutes(Music.mediaPlayer!!.duration)
        }
        else seekbar.max = 1


        // atualiza os nomes de artista e da musica e atualiza a imagem de cover
        musicTitle = findViewById(R.id.playerMusicTitle)
        musicArtist = findViewById(R.id.playerMusicArtist)
        musicImage = findViewById(R.id.playerCover)
        musicTitle.text = Music.currentPlaying!!.title
        musicArtist.text = Music.currentPlaying!!.artist
        musicImage.setImageBitmap(Music.currentPlaying!!.cover)


        // botao de play/pause
        playPauseButton = findViewById(R.id.playerPlayPauseButton)
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


        // botão de next
        nextButton = findViewById(R.id.playerNextButton)
        nextButton.setOnClickListener{
            Playlist.nextMusic(Music.currentPlaying!!, this)
        }

        //botão previous
        previousButton = findViewById(R.id.playerPreviousButton)
        previousButton.setOnClickListener{
            Playlist.previousMusic(Music.currentPlaying!!, this)
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

        }
    }

    fun millToMinutes(milliseconds: Int): String{
        val minutes = milliseconds / 1000 / 60
        val seconds = milliseconds / 1000 % 60
        return String.format("%02d:%02d",minutes,seconds)
    }


}