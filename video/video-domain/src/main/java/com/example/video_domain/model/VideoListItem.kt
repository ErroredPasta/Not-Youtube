package com.example.video_domain.model

import java.time.LocalDateTime

data class VideoListItem(
    val id: String,
    val thumbnail: String,
    val title: String,
    val channelThumbnail: String,
    val channelName: String,
    val view: Int,
    val dateTime: LocalDateTime,
    val length: Int,
)