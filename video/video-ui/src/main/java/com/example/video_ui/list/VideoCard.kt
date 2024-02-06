package com.example.video_ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core_ui.theme.NotYoutubeTheme
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

private val videoThumbnailHeight = 200.dp
private val channelThumbnailSize = 40.dp
private val smallPadding = 4.dp
private val normalPadding = 8.dp

@Composable
fun VideoCard(
    thumbnailUrl: String,
    videoTitle: String,
    channelThumbnailUrl: String,
    channelName: String,
    view: Int,
    dateTime: LocalDateTime,
    videoLength: Int,
    onVideoClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(normalPadding)
            .clickable(onClick = onVideoClick),
        elevation = CardDefaults.cardElevation(defaultElevation = smallPadding)
    ) {
        Box {
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = "Video Thumbnail",
                modifier = Modifier.height(videoThumbnailHeight),
                contentScale = ContentScale.Crop
            )

            Card(
                shape = RoundedCornerShape(smallPadding),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(smallPadding),
            ) {
                Text(
                    text = videoLength.toTimeString(),
                    modifier = Modifier.padding(smallPadding)
                )
            }
        }

        Row(
            modifier = Modifier.padding(normalPadding)
        ) {
            AsyncImage(
                model = channelThumbnailUrl,
                contentDescription = "Channel Thumbnail",
                modifier = Modifier
                    .size(channelThumbnailSize)
                    .clip(CircleShape)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = normalPadding)
            ) {
                Text(
                    text = videoTitle,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$channelName · 조회수 ${view}회 · ${dateTime.calculateTimeDifferenceFromNow()}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

private fun Int.toTimeString(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    val result = buildString {
        if (hours != 0) {
            append("%d:%02d:%02d".format(hours, minutes, seconds))
        } else {
            append("%d:%02d".format(minutes, seconds))
        }
    }

    return result
}

fun LocalDateTime.calculateTimeDifferenceFromNow(): String {
    val minutes = ChronoUnit.MINUTES.between(this, LocalDateTime.now())
    val hours = minutes / 60
    val days = hours / 24
    val months = ChronoUnit.MONTHS.between(this, LocalDateTime.now())
    val years = months / 12

    return when {
        years > 0 -> "${years}년 전"
        months > 0 -> "${months}개월 전"
        days > 0 -> "${days}일 전"
        hours > 0 -> "${hours}시간 전"
        else -> "${minutes}분 전"
    }
}

@Preview(showBackground = true)
@Composable
private fun VideoCardPreview() {
    NotYoutubeTheme {
        VideoCard(
            thumbnailUrl = "https://picsum.photos/seed/Blender/40",
            videoTitle = "video title video title video title video title video title video title video title video title ",
            channelThumbnailUrl = "",
            channelName = "Channel name",
            view = 23,
            dateTime = LocalDateTime.of(2024, 1, 24, 12, 0, 0),
            videoLength = 100,
            onVideoClick = {}
        )
    }
}