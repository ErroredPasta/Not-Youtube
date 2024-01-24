package com.example.notyoutube.data.remote

import com.example.notyoutube.data.dto.VideoDetailResponse
import com.example.notyoutube.data.dto.VideoListItemResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface VideoApi {
    @GET("videos")
    suspend fun getVideoList(): List<VideoListItemResponse>

    @GET("videos/{id}")
    suspend fun getVideoDetailById(@Path("id") id: String): VideoDetailResponse
}