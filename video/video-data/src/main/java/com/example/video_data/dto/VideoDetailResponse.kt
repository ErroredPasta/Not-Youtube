package com.example.video_data.dto

import com.google.gson.annotations.SerializedName

data class VideoDetailResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("video_url")
    val videoUrl: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("view")
    val view: Int,
    @SerializedName("date_time")
    val dateTime: String,
    @SerializedName("channel_name")
    val channelName: String,
    @SerializedName("channel_thumbnail")
    val channelThumbnail: String,
    @SerializedName("description")
    val description: String
)