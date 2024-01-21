package com.example.notyoutube.data.dto


import com.google.gson.annotations.SerializedName

data class VideoListItemResponse(
    @SerializedName("channel_name")
    val channelName: String,
    @SerializedName("channel_thumbnail")
    val channelThumbnail: String,
    @SerializedName("date_time")
    val dateTime: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("length")
    val length: Int,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("view")
    val view: Int
)