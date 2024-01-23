package com.example.notyoutube.data.repository

import com.example.notyoutube.data.dto.toDomain
import com.example.notyoutube.data.remote.VideoApi
import com.example.notyoutube.domain.model.VideoListItem
import com.example.notyoutube.domain.repository.VideoRepository
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val videoApi: VideoApi
) : VideoRepository {
    override suspend fun getVideoList(): List<VideoListItem> {
        return videoApi.getVideoList().toDomain()
    }
}