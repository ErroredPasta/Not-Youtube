package com.example.notyoutube.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notyoutube.domain.repository.VideoRepository
import com.example.notyoutube.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoListScreenViewModel @Inject constructor(
    private val repository: VideoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<State>(State.Loading)
    val uiState = _uiState.onSubscription {
        getVideoList()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 3_000),
        initialValue = State.Loading
    )

    fun getVideoList() {
        viewModelScope.launch {
            _uiState.update { State.Loading }

            val videoList = repository.getVideoList()
            _uiState.update { State.Success(data = videoList) }
        }
    }
}