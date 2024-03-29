package com.project.spass.presentation.screens.home_screen

import com.project.spass.R
import com.project.spass.presentation.graphs.home_graph.ShopHomeScreen

sealed class BottomNavItem(val title: String, val icon: Int, val route: String) {
    object PassNav : BottomNavItem(
        title = "Pass",
        icon = R.drawable.bill_icon,
        route = ShopHomeScreen.PassScreen.route
    )

    object HomeNav : BottomNavItem(
        title = "Home",
        icon = R.drawable.shop_icon,
        route = ShopHomeScreen.DashboardScreen.route
    )

    object ProfileNav : BottomNavItem(
        title = "Profile",
        icon = R.drawable.user_icon,
        route = ShopHomeScreen.ProfileScreen.route
    )
}
