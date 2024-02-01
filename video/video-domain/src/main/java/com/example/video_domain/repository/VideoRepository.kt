package com.example.video_domain.repository

import com.example.video_domain.model.VideoDetail
import com.example.video_domain.model.VideoListItem

interface VideoRepository {
    suspend fun getVideoList(): List<VideoListItem>
    suspend fun getVideoDetailById(id: String): VideoDetail
}