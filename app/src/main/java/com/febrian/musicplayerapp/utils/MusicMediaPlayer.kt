package com.febrian.musicplayerapp.utils

import android.media.MediaPlayer

object MusicMediaPlayer {

    @Volatile
    private var INSTANCE: MediaPlayer? = null

    @JvmStatic
    fun getInstance(): MediaPlayer {
        if (INSTANCE == null) {
            synchronized(MusicMediaPlayer::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = MediaPlayer()
                }
            }
        }
        return INSTANCE as MediaPlayer
    }

    var currentIndex = -1
}