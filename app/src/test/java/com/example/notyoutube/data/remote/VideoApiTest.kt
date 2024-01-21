package com.example.notyoutube.data.remote

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class VideoApiTest {
    private lateinit var sut: VideoApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer().also { it.start() }
        sut = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getVideoList_successfullyGetList_returnVideoList() = runTest {
        // arrange
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(VIDEO_LIST_CONTENT)
        )

        // act
        val videoList = sut.getVideoList()

        // assert
        val expectedChannelNames = (0..12).map { "channel name $it" }

        assertThat(videoList.size, `is`(expectedChannelNames.size))

        videoList.forEachIndexed { index, video ->
            assertThat(video.channelName, `is`(expectedChannelNames[index]))
        }
    }

    @Test
    fun getVideoList_internalServerError_throwException() = runTest {
        // arrange
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(INTERNAL_SERVER_ERROR_CODE)
                .setBody(INTERNAL_SERVER_ERROR_MESSAGE)
        )

        try {
            // act
            sut.getVideoList()
        } catch (e: HttpException) {
            // assert
            assertThat(e.code(), `is`(INTERNAL_SERVER_ERROR_CODE))
            assertThat(e.response()!!.errorBody()!!.string(), `is`(INTERNAL_SERVER_ERROR_MESSAGE))
            return@runTest
        }

        Assert.fail()
    }

    companion object {
        const val INTERNAL_SERVER_ERROR_CODE = 500
        const val INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error Occurred"
    }
}