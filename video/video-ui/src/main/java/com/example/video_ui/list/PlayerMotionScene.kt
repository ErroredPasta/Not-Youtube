package com.example.video_ui.list

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.Transition
import androidx.constraintlayout.compose.Visibility
import com.example.video_ui.detail.ConstraintIds

val expand = ConstraintSet {
    val player = createRefFor(ConstraintIds.PLAYER)
    val playerContainer = createRefFor(ConstraintIds.PLAYER_CONTAINER)
    val title = createRefFor(ConstraintIds.TITLE)
    val channelName = createRefFor(ConstraintIds.CHANNEL_NAME)
    val playPauseButton = createRefFor(ConstraintIds.PLAY_PAUSE_BUTTON)
    val hideButton = createRefFor(ConstraintIds.HIDE_BUTTON)
    val titleAndVideoListContainer = createRefFor(ConstraintIds.TITLE_AND_VIDEO_LIST_CONTAINER)


    constrain(player) {
        width = Dimension.matchParent
        height = Dimension.ratio("16:9")

        top.linkTo(parent.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
    }

    constrain(playerContainer) {
        width = Dimension.matchParent
        height = Dimension.matchParent
    }

    constrain(title) {
        alpha = 0f
        absoluteLeft.linkTo(parent.absoluteRight)
    }

    constrain(channelName) {
        alpha = 0f
        absoluteLeft.linkTo(parent.absoluteRight)
    }

    constrain(playPauseButton) {
        alpha = 0f
        absoluteLeft.linkTo(parent.absoluteRight)
    }

    constrain(hideButton) {
        alpha = 0f
        absoluteLeft.linkTo(parent.absoluteRight)
    }

    constrain(titleAndVideoListContainer) {
        width = Dimension.matchParent
        height = Dimension.fillToConstraints

        alpha = 1f
        visibility = Visibility.Visible

        top.linkTo(player.bottom)
        bottom.linkTo(parent.bottom)
    }
}


val collapse = ConstraintSet {
    val player = createRefFor(ConstraintIds.PLAYER)
    val playerContainer = createRefFor(ConstraintIds.PLAYER_CONTAINER)
    val title = createRefFor(ConstraintIds.TITLE)
    val channelName = createRefFor(ConstraintIds.CHANNEL_NAME)
    val playPauseButton = createRefFor(ConstraintIds.PLAY_PAUSE_BUTTON)
    val hideButton = createRefFor(ConstraintIds.HIDE_BUTTON)
    val titleAndVideoListContainer = createRefFor(ConstraintIds.TITLE_AND_VIDEO_LIST_CONTAINER)


    constrain(player) {
        width = Dimension.ratio("5:2")
        height = Dimension.fillToConstraints

        top.linkTo(playerContainer.top)
        bottom.linkTo(playerContainer.bottom)
    }

    constrain(playerContainer) {
        width = Dimension.matchParent
        height = 56.dp.asDimension

        bottom.linkTo(parent.bottom)
    }


    constrain(title) {
        alpha = 1f

        absoluteLeft.linkTo(player.absoluteRight, margin = 8.dp)
        top.linkTo(playerContainer.top)
        bottom.linkTo(channelName.top)
    }

    constrain(channelName) {
        alpha = 1f

        absoluteLeft.linkTo(player.absoluteRight, margin = 8.dp)
        top.linkTo(title.bottom)
        bottom.linkTo(playerContainer.bottom)
    }

    constrain(playPauseButton) {
        alpha = 1f

        top.linkTo(playerContainer.top)
        bottom.linkTo(playerContainer.bottom)
        absoluteRight.linkTo(hideButton.absoluteLeft, margin = 8.dp)
    }

    constrain(hideButton) {
        alpha = 1f

        top.linkTo(playerContainer.top)
        bottom.linkTo(playerContainer.bottom)
        absoluteRight.linkTo(playerContainer.absoluteRight, margin = 8.dp)
    }

    constrain(titleAndVideoListContainer) {
        alpha = 0f

        width = Dimension.matchParent
        height = Dimension.fillToConstraints

        top.linkTo(parent.bottom)
        bottom.linkTo(parent.bottom)
    }
}


@OptIn(ExperimentalMotionApi::class)
val expandToCollapse = Transition(from = Names.EXPAND, to = Names.COLLAPSE) {}

@OptIn(ExperimentalMotionApi::class)
val motionScene = MotionScene {
    addConstraintSet(expand, Names.EXPAND)
    addConstraintSet(collapse, Names.COLLAPSE)
    addTransition(expandToCollapse, Names.EXPAND_TO_COLLAPSE)
}

object Names {
    const val EXPAND = "expand"
    const val COLLAPSE = "collapse"
    const val EXPAND_TO_COLLAPSE = "default"
}