@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.video_ui.detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.core_ui.theme.NotYoutubeTheme
import com.example.core_util.State
import com.example.video_domain.model.VideoListItem
import com.example.video_ui.list.VideoCard
import com.example.video_ui.list.calculateTimeDifferenceFromNow
import java.time.LocalDateTime

private val titlePadding = 16.dp
private val titleBodySpace = 4.dp
private val descriptionSheetPadding = 8.dp

@Composable
fun VideoDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: VideoDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state is State.Success) {
        val uiState = (state as State.Success).data

        val player = ExoPlayer.Builder(LocalContext.current).build().apply {
            setMediaItem(MediaItem.fromUri(uiState.videoDetail.videoUrl))
        }

        VideoDetailScreenBody(
            uiState = uiState,
            modifier = modifier,
            player = player
        )
    }
}

@Composable
fun VideoDetailScreenBody(
    uiState: VideoDetailUiState,
    modifier: Modifier = Modifier,
    player: Player,
) {
    val scrollState = rememberScrollState()
    var showDescriptionSheet by remember { mutableStateOf(false) }

    BackHandler(enabled = showDescriptionSheet) {
        showDescriptionSheet = false
    }

    DisposableEffect(player) {
        player.prepare()
        onDispose { player.release() }
    }

    Column(modifier = modifier) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply { setPlayer(player) }
            },
            modifier = Modifier.aspectRatio(16 / 9f)
        )

        Box {
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                VideoTitle(
                    title = uiState.videoDetail.title,
                    view = uiState.videoDetail.view,
                    dateTime = uiState.videoDetail.dateTime,
                    onClick = { showDescriptionSheet = true }
                )
                VideoList(videoList = uiState.videoList)
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = showDescriptionSheet,
                enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }),
            ) {
                DescriptionSheet(
                    description = uiState.videoDetail.description,
                    onClose = { showDescriptionSheet = false }
                )
            }
        }
    }
}

@Composable
fun DescriptionSheet(
    description: String,
    onClose: () -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = descriptionSheetPadding)
            ) {
                Text(
                    "설명",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onClose,
                ) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Close button")
                }
            }

            Divider()

            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(descriptionSheetPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DescriptionSheetPreview() {
    NotYoutubeTheme {
        DescriptionSheet(description = "description", onClose = {})
    }
}

@Composable
fun VideoTitle(
    title: String,
    view: Int,
    dateTime: LocalDateTime,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(titlePadding)
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(titleBodySpace))

        Row(
            horizontalArrangement = Arrangement.spacedBy(titleBodySpace)
        ) {
            Text("조회수 ${view}회", style = MaterialTheme.typography.bodyMedium)
            Text(
                dateTime.calculateTimeDifferenceFromNow(),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "더 보기",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TitlePreview() {
    NotYoutubeTheme {
        VideoTitle(
            title = "Title",
            view = 30000,
            dateTime = LocalDateTime.of(2024, 1, 24, 12, 0, 0),
            onClick = {}
        )
    }
}

@Composable
fun VideoList(
    videoList: List<VideoListItem>,
) {
    videoList.forEach {
        VideoCard(
            thumbnailUrl = it.thumbnail,
            videoTitle = it.title,
            channelThumbnailUrl = it.channelThumbnail,
            channelName = it.channelName,
            view = it.view,
            dateTime = it.dateTime,
            videoLength = it.length,
            onVideoClick = {}
        )
    }
}
