package com.example.video_ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_util.State
import com.example.video_domain.model.VideoDetail
import com.example.video_domain.model.VideoListItem
import com.example.video_domain.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoDetailViewModel @Inject constructor(
    private val repository: VideoRepository,
) : ViewModel() {
    private val videoDetail = MutableStateFlow<State<VideoDetail>>(State.Loading)
    private val videoList = MutableStateFlow<State<List<VideoListItem>>>(State.Loading)

    val uiState = combine(videoDetail, videoList) { videoDetail, videoList ->
        when {
            videoDetail == State.Loading || videoList == State.Loading -> State.Loading
            videoDetail is State.Error -> State.Error(error = videoDetail.error)
            videoList is State.Error -> State.Error(error = videoList.error)
            else -> {
                videoDetail as State.Success
                videoList as State.Success

                State.Success(
                    VideoDetailUiState(
                        videoDetail = videoDetail.data,
                        videoList = videoList.data
                    )
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 3_000),
        initialValue = State.Loading
    )

    private val videoDetailExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        videoDetail.update { State.Error(error = throwable) }
    }

    private val videoListExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        videoList.update { State.Error(error = throwable) }
    }

    private fun getVideoDetail(id: String) {
        viewModelScope.launch(videoDetailExceptionHandler) {
            videoDetail.update { State.Success(repository.getVideoDetailById(id = id)) }
        }
    }

    private fun getVideoList() {
        viewModelScope.launch(videoListExceptionHandler) {
            videoList.update { State.Success(repository.getVideoList()) }
        }
    }

    fun load(id: String) {
        getVideoDetail(id = id)
        getVideoList()
    }
}