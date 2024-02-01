package com.example.video_data.dto

import com.example.video_domain.model.VideoDetail
import com.example.video_domain.model.VideoListItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun VideoListItemResponse.toDomain(): VideoListItem = VideoListItem(
    id = id,
    thumbnail = thumbnail,
    title = title,
    channelThumbnail = channelThumbnail,
    channelName = channelName,
    view = view,
    dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").run {
        LocalDateTime.parse(dateTime, this)
    },
    length = length
)

fun List<VideoListItemResponse>.toDomain(): List<VideoListItem> = map { it.toDomain() }

fun VideoDetailResponse.toDomain() = VideoDetail(
    id = id,
    videoUrl = videoUrl,
    title = title,
    view = view,
    dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").run {
        LocalDateTime.parse(dateTime, this)
    },
    channelName = channelName,
    channelThumbnail = channelThumbnail,
    description = description
)