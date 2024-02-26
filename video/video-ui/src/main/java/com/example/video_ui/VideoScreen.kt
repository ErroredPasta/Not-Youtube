package com.example.video_ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayoutState
import androidx.constraintlayout.compose.rememberMotionLayoutState
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.core_ui.theme.NotYoutubeTheme
import com.example.video_domain.model.VideoListItem
import com.example.video_ui.detail.VideoDetailScreen
import com.example.video_ui.list.VideoListScreen

@OptIn(ExperimentalMotionApi::class)
@Composable
fun VideoScreen(
    player: Player,
    modifier: Modifier = Modifier,
) {
    var showDetailScreen by remember { mutableStateOf(false) }
    var videoDetailId by remember { mutableStateOf("") }
    val motionLayoutState = rememberMotionLayoutState()
    val onVideoItemClick = { it: VideoListItem ->
        videoDetailId = it.id

        if (showDetailScreen) {
            motionLayoutState.expand()
        } else {
            showDetailScreen = true
            motionLayoutState.snapTo(0f)
        }

        player.play()
    }

    Box(modifier = modifier) {
        VideoListScreen(
            onClick = onVideoItemClick
        )

        AnimatedVisibility(
            visible = showDetailScreen,
            enter = EnterTransition.None,
            exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }),
        ) {
            VideoDetailScreen(
                id = videoDetailId,
                player = player,
                modifier = Modifier.fillMaxSize(),
                motionLayoutState = motionLayoutState,
                onVideoItemClick = onVideoItemClick,
                onCloseClick = { showDetailScreen = false }
            )
        }
    }
}

@OptIn(ExperimentalMotionApi::class)
fun MotionLayoutState.collapse() {
    animateTo(1f, tween())
}

@OptIn(ExperimentalMotionApi::class)
fun MotionLayoutState.expand() {
    animateTo(0f, tween())
}

@Preview(showBackground = true)
@Composable
private fun VideoScreenPreview() {
    NotYoutubeTheme {
        VideoScreen(player = ExoPlayer.Builder(LocalContext.current).build())
    }
}
