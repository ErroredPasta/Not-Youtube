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
    onClick: (VideoListItem) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    VideoListScreenBody(state = state, onClick = onClick)
}

@Composable
fun VideoListScreenBody(
    state: State<List<VideoListItem>>,
    onClick: (VideoListItem) -> Unit
) {
    if (state is State.Success<List<VideoListItem>>) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = state.data,
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
                    onVideoClick = { onClick(item) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VideoListScreenPreview() {
    NotYoutubeTheme {
        VideoListScreenBody(state = State.Loading, onClick = {})
    }
}