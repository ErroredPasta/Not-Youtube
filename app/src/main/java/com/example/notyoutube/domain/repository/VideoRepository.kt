package com.example.notyoutube.domain.repository

import com.example.notyoutube.domain.model.VideoListItem

interface VideoRepository {
    suspend fun getVideoList(): List<VideoListItem>
}