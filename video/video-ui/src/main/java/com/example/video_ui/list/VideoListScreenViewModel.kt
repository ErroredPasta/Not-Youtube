package com.example.video_ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_util.State
import com.example.video_domain.model.VideoListItem
import com.example.video_domain.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoListScreenViewModel @Inject constructor(
    private val repository: VideoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<State<List<VideoListItem>>>(State.Loading)
    val uiState = _uiState.onSubscription {
        getVideoList()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 3_000),
        initialValue = State.Loading
    )

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.update { State.Error(error = throwable) }
    }

    fun getVideoList() {
        viewModelScope.launch(exceptionHandler) {
            _uiState.update { State.Loading }

            val videoList = repository.getVideoList()
            _uiState.update { State.Success(data = videoList) }
        }
    }
}