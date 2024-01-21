package com.example.notyoutube.data.repository

import com.example.notyoutube.data.remote.VideoApi
import com.example.notyoutube.domain.model.VideoListItem
import com.example.notyoutube.domain.repository.VideoRepository
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    videoApi: VideoApi
) : VideoRepository {
    override suspend fun getVideoList(): List<VideoListItem> {
        return emptyList()
    }
}