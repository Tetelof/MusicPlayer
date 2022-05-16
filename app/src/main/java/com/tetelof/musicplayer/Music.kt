package com.tetelof.musicplayer

import android.net.Uri

data class Music(
    val id: Long,
    val title: String,
    val path: Uri
)
