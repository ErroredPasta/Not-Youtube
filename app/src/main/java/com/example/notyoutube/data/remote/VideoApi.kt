package com.example.notyoutube.data.remote

import com.example.notyoutube.data.dto.VideoListItemResponse
import retrofit2.http.GET


interface VideoApi {
    @GET("videos")
    suspend fun getVideoList(): List<VideoListItemResponse>
}