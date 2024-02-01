package com.example.video_data.remote

import com.example.video_data.dto.VideoDetailResponse
import com.example.video_data.dto.VideoListItemResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FakeVideoApi(private val gson: Gson) : VideoApi {
    private val videoList = run<List<VideoListItemResponse>> {
        val typeToken = object : TypeToken<List<VideoListItemResponse>>() {}.type
        gson.fromJson(VIDEO_LIST_CONTENT, typeToken)
    }

    override suspend fun getVideoList(): List<VideoListItemResponse> {
        return videoList
    }

    override suspend fun getVideoDetailById(id: String): VideoDetailResponse {
        val index = videoList.indexOfFirst { it.id == id }

        if (index == -1) throw NoSuchElementException()

        return VideoDetailResponse(
            id = id,
            videoUrl = VIDEO_URLS[index],
            title = videoList[index].title,
            view = videoList[index].view,
            dateTime = videoList[index].dateTime,
            channelName = videoList[index].channelName,
            channelThumbnail = videoList[index].channelThumbnail,
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum ultricies ultrices imperdiet. Duis eget tortor ut erat suscipit gravida. In ex enim, laoreet vitae nisl faucibus, laoreet ullamcorper ex. Praesent vehicula laoreet efficitur. Integer aliquet odio ac ante congue varius. Donec facilisis mauris non neque ornare, in viverra velit fermentum. Nam dictum metus ac orci egestas, ut consectetur quam dapibus. Maecenas efficitur blandit finibus. Sed dignissim dolor risus, sit amet elementum tortor viverra quis. Praesent quis gravida augue, id bibendum nulla."
        )
    }
}