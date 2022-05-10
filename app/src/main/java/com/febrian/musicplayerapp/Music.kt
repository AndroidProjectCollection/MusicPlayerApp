package com.febrian.musicplayerapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Music(
    val path: String,
    val title: String,
    val duration: String
) : Parcelable
