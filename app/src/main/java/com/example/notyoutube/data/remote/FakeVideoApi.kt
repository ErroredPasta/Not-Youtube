package com.example.notyoutube.data.remote

import com.example.notyoutube.data.dto.VideoListItemResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FakeVideoApi(private val gson: Gson) : VideoApi {
    override suspend fun getVideoList(): List<VideoListItemResponse> {
        val typeToken = object : TypeToken<List<VideoListItemResponse>>() {}.type
        return gson.fromJson(VIDEO_LIST_CONTENT, typeToken)
    }
}