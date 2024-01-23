package com.example.notyoutube.data.dto

import com.example.notyoutube.domain.model.VideoListItem
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