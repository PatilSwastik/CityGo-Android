package com.project.spass.presentation.graphs.home_graph


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.spass.presentation.graphs.Graph
import com.project.spass.presentation.graphs.detail_graph.DetailScreen
import com.project.spass.presentation.graphs.detail_graph.detailNavGraph
import com.project.spass.presentation.screens.dashboard_screen.component.DashboardScreen
import com.project.spass.presentation.screens.pass_screen.compnent.PassScreen
import com.project.spass.presentation.screens.profile_screen.component.ProfileScreen

@Composable
fun HomeNavGraph(navHostController: NavHostController, searchText : String) {
    NavHost(
        navController = navHostController,
        route = Graph.HOME,
        startDestination = ShopHomeScreen.DashboardScreen.route
    ) {
        composable(ShopHomeScreen.DashboardScreen.route) {
            DashboardScreen(searchText = searchText) { passId ->
                navHostController.navigate(DetailScreen.PassDetailScreen.route + "/${passId}")
            }
        }
        composable(ShopHomeScreen.PassScreen.route) {
            PassScreen()
        }
        composable(ShopHomeScreen.ProfileScreen.route) {
            ProfileScreen() {
                navHostController.popBackStack()
            }
        }
        //detail graph
        detailNavGraph(navController = navHostController)
    }
}