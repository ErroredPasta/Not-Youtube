package com.example.video_ui.detail

import com.example.video_domain.model.VideoDetail
import com.example.video_domain.model.VideoListItem

data class VideoDetailUiState(
    val videoDetail: VideoDetail,
    val videoList: List<VideoListItem>,
)
