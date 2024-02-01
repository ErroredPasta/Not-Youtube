package com.example.video_data.repository

import com.example.video_domain.model.VideoDetail
import com.example.video_data.dto.toDomain
import com.example.video_data.remote.VideoApi
import com.example.video_domain.model.VideoListItem
import com.example.video_domain.repository.VideoRepository
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val videoApi: VideoApi
) : VideoRepository {
    override suspend fun getVideoList(): List<VideoListItem> {
        return videoApi.getVideoList().toDomain()
    }

    override suspend fun getVideoDetailById(id: String): VideoDetail {
        return videoApi.getVideoDetailById(id).toDomain()
    }
}