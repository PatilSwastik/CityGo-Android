package com.project.spass.presentation.graphs.detail_graph

sealed class DetailScreen( val route: String) {
    object NotificationScreen : DetailScreen("notification_screen")
    object PassDetailScreen : DetailScreen("pass_detail_screen")
}