package com.example.video_ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.video_ui.list.VideoListScreen

fun NavGraphBuilder.videoGraph(navController: NavController) {
    navigation(startDestination = "video_list", route = "video") {
        composable(route = "video_list") {
            VideoListScreen()
        }
    }
}