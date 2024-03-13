package com.eritlab.jexmon.presentation.graphs.detail_graph

sealed class DetailScreen( val route: String) {
    object NotificationScreen : DetailScreen("notification_screen")
    object ProductDetailScreen : DetailScreen("product_detail_screen")
}