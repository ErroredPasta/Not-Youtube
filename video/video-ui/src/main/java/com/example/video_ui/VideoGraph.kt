package com.example.video_ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.video_ui.detail.VideoDetailScreen
import com.example.video_ui.list.VideoListScreen

fun NavGraphBuilder.videoGraph(navController: NavController) {
    navigation(startDestination = "video_list", route = "video") {
        composable(route = "video_list") {
            VideoListScreen(onClick = { navController.navigate("video/${it.id}") })
        }

        composable(
            route = "video/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            VideoDetailScreen()
        }
    }
}