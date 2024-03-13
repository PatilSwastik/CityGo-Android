package com.eritlab.jexmon.presentation.graphs.home_graph

sealed class ShopHomeScreen(val route: String) {
    object PassScreen : ShopHomeScreen("pass_screen")
    object DashboardScreen : ShopHomeScreen("dashboard_screen")
    object ProfileScreen : ShopHomeScreen("profile_screen")
}
