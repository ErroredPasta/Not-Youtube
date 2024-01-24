package com.example.notyoutube.domain.repository

import com.example.notyoutube.domain.model.VideoDetail
import com.example.notyoutube.domain.model.VideoListItem

interface VideoRepository {
    suspend fun getVideoList(): List<VideoListItem>
    suspend fun getVideoDetailById(id: String): VideoDetail
}