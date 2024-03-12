package com.example.video_ui.list

import com.example.core_util.State
import com.example.video_domain.model.VideoListItem
import com.example.video_domain.repository.VideoRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime


@OptIn(ExperimentalCoroutinesApi::class)
class VideoListScreenViewModelTest {
    private lateinit var sut: VideoListScreenViewModel
    private lateinit var videoRepository: VideoRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        videoRepository = mockk()
        sut = VideoListScreenViewModel(videoRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `collecting uiState should update uiState to Loading and Success`() = runTest {
        val videoList = listOf(
            VideoListItem(
                id = "1",
                thumbnail = "Title 1",
                title = "Description 1",
                channelThumbnail = "",
                channelName = "",
                view = 123,
                dateTime = LocalDateTime.now(),
                length = 123
            )
        )

        coEvery { videoRepository.getVideoList() } returns videoList

        collectAndCancel(sut.uiState) {

            assertThat(sut.uiState.value).isEqualTo(State.Loading)
            advanceUntilIdle()

            assertThat(sut.uiState.value).isEqualTo(State.Success(videoList))
        }

    }

    @Test
    fun `collecting uiState should update uiState to Loading and Error when repository throws exception`() =
        runTest {
            coEvery { videoRepository.getVideoList() } throws RuntimeException("Error loading videos")

            collectAndCancel(sut.uiState) {
                assertThat(sut.uiState.value).isEqualTo(State.Loading)
                advanceUntilIdle()

                assertThat(sut.uiState.value).isInstanceOf(State.Error::class.java)
            }
        }

    fun <T> TestScope.collectAndCancel(flow: Flow<T>, block: TestScope.() -> Unit) {
        val collectJob = launch { flow.collect() }

        block()

        collectJob.cancel()
    }
}