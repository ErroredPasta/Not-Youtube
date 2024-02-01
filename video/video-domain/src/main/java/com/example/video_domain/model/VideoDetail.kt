package com.example.video_domain.model

import java.time.LocalDateTime

data class VideoDetail(
    val id: String,
    val videoUrl: String,
    val title: String,
    val view: Int,
    val dateTime: LocalDateTime,
    val channelName: String,
    val channelThumbnail: String,
    val description: String
)
