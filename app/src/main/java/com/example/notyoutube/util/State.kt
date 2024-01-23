package com.example.notyoutube.util

sealed interface State {
    data object Loading : State
    data class Success<T>(val data: T) : State
    data class Error(val error: Throwable) : State
}