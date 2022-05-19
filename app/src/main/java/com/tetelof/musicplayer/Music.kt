package com.tetelof.musicplayer

import android.net.Uri

data class Music(
    val id: Long,
    val title: String,
    val author: String,
    val path: Uri
)
