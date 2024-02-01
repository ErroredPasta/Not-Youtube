package com.example.video_ui.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.video_domain.model.VideoListItem
import com.example.core_ui.theme.NotYoutubeTheme
import com.example.core_util.State

@Composable
fun VideoListScreen(
    viewModel: VideoListScreenViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    VideoListScreenBody(state = state)
}

@Composable
fun VideoListScreenBody(
    state: State,
) {
    if (state is State.Success<*>) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            @Suppress("UNCHECKED_CAST")
            items(
                items = state.data as List<VideoListItem>,
                key = { item -> item.id }
            ) { item ->
                VideoCard(
                    thumbnailUrl = item.thumbnail,
                    videoTitle = item.title,
                    channelThumbnailUrl = item.channelThumbnail,
                    channelName = item.channelName,
                    view = item.view,
                    dateTime = item.dateTime,
                    videoLength = item.length,
                    onVideoClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VideoListScreenPreview() {
    NotYoutubeTheme {
        VideoListScreenBody(state = State.Loading)
    }
}