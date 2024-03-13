package com.project.spass.presentation.graphs.detail_graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.project.spass.common.Constrains
import com.project.spass.presentation.graphs.Graph
import com.project.spass.presentation.screens.notification_screen.component.NotificationScreen
import com.project.spass.presentation.screens.pass_detail_screen.component.PassDetailScreen

fun NavGraphBuilder.detailNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAILS,
        startDestination = DetailScreen.PassDetailScreen.route + "/{${Constrains.PASS_ID_PARAM}}"
    ) {
        composable(DetailScreen.NotificationScreen.route) {
            NotificationScreen()
        }
        composable(DetailScreen.PassDetailScreen.route + "/{passId}") {
            PassDetailScreen() {
                navController.popBackStack()
            }
        }
    }
}