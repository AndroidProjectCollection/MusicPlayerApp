package com.febrian.musicplayerapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.febrian.musicplayerapp.databinding.ActivityMusicPlayerBinding
import java.io.IOException
import java.util.concurrent.TimeUnit


class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMusicPlayerBinding

    private lateinit var currentMusic: Music
    var mediaPlayer: MediaPlayer = MusicMediaPlayer.getInstance()
    private var listMusic: ArrayList<Music> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.musicTitle.isSelected = true

        listMusic = intent.getParcelableArrayListExtra<Parcelable>("LIST") as ArrayList<Music>

        setResourcesWithMusic()

        runOnUiThread(object : Runnable {
            override fun run() {
                binding.seekBar.progress = mediaPlayer.currentPosition
                binding.currentTime.text =
                    convertToMMSS(mediaPlayer.currentPosition.toString() + "")
                if (mediaPlayer.isPlaying) {
                    binding.pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                } else {
                    binding.pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                }
                Handler().postDelayed(this, 100)
            }
        })

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

    }

    private fun setResourcesWithMusic() {
        currentMusic = listMusic[MusicMediaPlayer.currentIndex]

        binding.musicTitle.text = currentMusic.title

        binding.totalTime.text = convertToMMSS(currentMusic.duration)

        binding.pausePlay.setOnClickListener { pausePlay() }
        binding.next.setOnClickListener { playNextSong() }
        binding.previous.setOnClickListener { playPreviousSong() }

        playMusic()
    }

    private fun playMusic() {
        mediaPlayer.reset()
        try {
            mediaPlayer.setDataSource(currentMusic.path)
            mediaPlayer.prepare()
            mediaPlayer.start()
            binding.seekBar.progress = 0
            binding.seekBar.max = mediaPlayer.duration
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun playNextSong() {
        if (MusicMediaPlayer.currentIndex == listMusic.size - 1) return
        MusicMediaPlayer.currentIndex += 1
        mediaPlayer.reset()
        setResourcesWithMusic()
    }

    private fun playPreviousSong() {
        if (MusicMediaPlayer.currentIndex == 0) return
        MusicMediaPlayer.currentIndex -= 1
        mediaPlayer.reset()
        setResourcesWithMusic()
    }

    private fun pausePlay() {
        if (mediaPlayer.isPlaying) mediaPlayer.pause() else mediaPlayer.start()
    }

    private fun convertToMMSS(duration: String): String {
        val millis = duration.toLong()
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)
        )
    }

}