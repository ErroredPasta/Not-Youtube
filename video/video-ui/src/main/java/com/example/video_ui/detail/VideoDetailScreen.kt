package com.example.video_ui.detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionLayoutState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import com.example.core_ui.theme.NotYoutubeTheme
import com.example.core_util.State
import com.example.video_domain.model.VideoListItem
import com.example.video_ui.R
import com.example.video_ui.collapse
import com.example.video_ui.expand
import com.example.video_ui.list.VideoCard
import com.example.video_ui.list.calculateTimeDifferenceFromNow
import com.example.video_ui.list.motionScene
import java.time.LocalDateTime

private val titlePadding = 16.dp
private val titleBodySpace = 4.dp
private val descriptionSheetPadding = 8.dp

@OptIn(ExperimentalMotionApi::class)
@Composable
fun VideoDetailScreen(
    id: String,
    player: Player,
    modifier: Modifier = Modifier,
    motionLayoutState: MotionLayoutState,
    onVideoItemClick: (VideoListItem) -> Unit,
    onCloseClick: () -> Unit,
    viewModel: VideoDetailViewModel = hiltViewModel(),
) {
    SideEffect {
        viewModel.load(id = id)
        player.prepare()
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state is State.Success) {
        val videoDetailUiState = (state as State.Success).data

        player.setMediaItem(MediaItem.fromUri(videoDetailUiState.videoDetail.videoUrl))

        VideoDetailScreenBody(
            modifier = modifier,
            motionLayoutState = motionLayoutState,
            player = player,
            uiState = videoDetailUiState,
            onVideoItemClick = onVideoItemClick,
            onCloseClick = onCloseClick
        )
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun VideoDetailScreenBody(
    modifier: Modifier = Modifier,
    motionLayoutState: MotionLayoutState,
    player: Player,
    uiState: VideoDetailUiState,
    onVideoItemClick: (VideoListItem) -> Unit,
    onCloseClick: () -> Unit,
) {
    var isPlaying by remember { mutableStateOf(player.isPlaying) }

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                super.onIsPlayingChanged(playing)
                isPlaying = playing
            }
        }

        player.addListener(listener)
        onDispose { player.removeListener(listener) }
    }

    BackHandler(enabled = motionLayoutState.currentProgress != 1f) {
        motionLayoutState.collapse()
    }

    MotionLayout(
        motionScene = motionScene,
        motionLayoutState = motionLayoutState,
        modifier = modifier
    ) {
        VideoPlayerContainer(onClick = motionLayoutState::expand)
        VideoPlayer(player = player)
        TitleAndVideoListContainer(uiState = uiState, onVideoItemClick = onVideoItemClick)
        TitleWhenCollapsed(title = uiState.videoDetail.title)
        ChannelNameWhenCollapsed(channelName = uiState.videoDetail.channelName)
        PlayButtonWhenCollapsed(
            isPlaying = isPlaying,
            onClick = { if (isPlaying) player.pause() else player.play() })
        CloseButtonWhenCollapse(onCloseClick = onCloseClick)
    }
}

@Composable
fun VideoPlayerContainer(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .clickable(onClick = onClick)
            .layoutId(ConstraintIds.PLAYER_CONTAINER)
    ) {}
}

@Composable
fun VideoPlayer(player: Player) {
    AndroidView(
        factory = { context ->
            PlayerView(context).apply { setPlayer(player) }
        },
        modifier = Modifier
            .layoutId(ConstraintIds.PLAYER)
    )
}

@Composable
fun TitleAndVideoListContainer(
    uiState: VideoDetailUiState,
    onVideoItemClick: (VideoListItem) -> Unit,
) {
    val scrollState = rememberScrollState()
    var showDescriptionSheet by remember { mutableStateOf(false) }

    BackHandler(enabled = showDescriptionSheet) {
        showDescriptionSheet = false
    }

    Box(modifier = Modifier.layoutId(ConstraintIds.TITLE_AND_VIDEO_LIST_CONTAINER)) {
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            VideoTitle(
                title = uiState.videoDetail.title,
                view = uiState.videoDetail.view,
                dateTime = uiState.videoDetail.dateTime,
                onClick = { showDescriptionSheet = true }
            )
            VideoList(videoList = uiState.videoList, onVideoItemClick = onVideoItemClick)
        }

        AnimatedVisibility(
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

@Composable
fun TitleWhenCollapsed(
    title: String,
) {
    Text(
        title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Normal,
        modifier = Modifier.layoutId(ConstraintIds.TITLE)
    )
}

@Composable
fun ChannelNameWhenCollapsed(
    channelName: String,
) {
    Text(
        channelName,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Normal,
        modifier = Modifier.layoutId(ConstraintIds.CHANNEL_NAME)
    )
}

@Composable
fun PlayButtonWhenCollapsed(
    isPlaying: Boolean,
    onClick: () -> Unit,
) {
    Crossfade(
        targetState = isPlaying,
        label = "play/pause button",
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .layoutId(ConstraintIds.PLAY_PAUSE_BUTTON)
    ) { playing ->
        if (playing) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_pause_24),
                contentDescription = "play button",
            )
        } else {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "pause button",
            )
        }
    }
}

@Composable
fun CloseButtonWhenCollapse(onCloseClick: () -> Unit) {
    Icon(
        imageVector = Icons.Filled.Close,
        contentDescription = "close button",
        modifier = Modifier
            .clickable(onClick = onCloseClick)
            .layoutId(ConstraintIds.HIDE_BUTTON)
    )
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
    onVideoItemClick: (VideoListItem) -> Unit,
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
            onVideoClick = { onVideoItemClick(it) }
        )
    }
}
