package com.example.core_util

sealed interface State<out T> {
    data object Loading : State<Nothing>
    data class Success<T>(val data: T) : State<T>
    data class Error(val error: Throwable) : State<Nothing>
}