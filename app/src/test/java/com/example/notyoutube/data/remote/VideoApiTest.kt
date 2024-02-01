package com.example.notyoutube.data.remote

import com.example.video_data.remote.VideoApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
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

    @Test
    fun getVideoDetailById_successfullyGetDetail_returnVideoDetail() = runTest {
        // arrange
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(VIDEO_DETAIL)
        )

        // act
        val videoDetail = sut.getVideoDetailById(id = VIDEO_ID)

        // assert
        assertThat(videoDetail.id, `is`(VIDEO_ID))
    }

    @Test
    fun getVideoDetailById_videoWithIdNotExists_throw404Exception() = runTest {
        // arrange
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(NOT_FOUND_ERROR_CODE)
        )

        try {
            // act
            sut.getVideoDetailById(id = VIDEO_ID_NOT_EXIST)
        } catch (e: HttpException) {
            // assert
            assertThat(e.code(), `is`(NOT_FOUND_ERROR_CODE))
            return@runTest
        }

        Assert.fail()
    }

    @Test
    fun getVideoDetailById_internalServerError_throw500Exception() = runTest {
        // arrange
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(INTERNAL_SERVER_ERROR_CODE)
        )

        try {
            // act
            sut.getVideoDetailById(id = VIDEO_ID)
        } catch (e: HttpException) {
            // assert
            assertThat(e.code(), `is`(INTERNAL_SERVER_ERROR_CODE))
            return@runTest
        }

        Assert.fail()
    }

    companion object {
        const val INTERNAL_SERVER_ERROR_CODE = 500
        const val NOT_FOUND_ERROR_CODE = 404
        const val INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error Occurred"
        const val VIDEO_ID = "fbcabb87-a0bf-4cb6-8fde-9386fbf5793f"
        const val VIDEO_ID_NOT_EXIST = "not exists"
        const val VIDEO_DETAIL = """
            {
              "id": "fbcabb87-a0bf-4cb6-8fde-9386fbf5793f",
              "video_url": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
              "title": "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! In the typical cartoon tradition he prepares the nasty rodents a comical revenge.\n\nLicensed under the Creative Commons Attribution license\nhttps://www.bigbuckbunny.org",
              "view": 100000,
              "date_time": "2024-01-22 10:11:12",
              "channel_name": "channel name 0",
              "channel_thumbnail": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg",
              "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum ultricies ultrices imperdiet. Duis eget tortor ut erat suscipit gravida. In ex enim, laoreet vitae nisl faucibus, laoreet ullamcorper ex. Praesent vehicula laoreet efficitur. Integer aliquet odio ac ante congue varius. Donec facilisis mauris non neque ornare, in viverra velit fermentum. Nam dictum metus ac orci egestas, ut consectetur quam dapibus. Maecenas efficitur blandit finibus. Sed dignissim dolor risus, sit amet elementum tortor viverra quis. Praesent quis gravida augue, id bibendum nulla."
            }
            """
    }
}