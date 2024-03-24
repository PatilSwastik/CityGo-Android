package com.project.spass.presentation.graphs.root_graph

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.spass.presentation.graphs.Graph
import com.project.spass.presentation.graphs.authNavGraph
import com.project.spass.presentation.screens.home_screen.component.HomeScreen
import com.project.spass.presentation.screens.sign_in_screen.AuthViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Composable
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
fun RootNavigationGraph(navHostController: NavHostController, context: Context,authViewModel : AuthViewModel) {
    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION,
    ) {
        authNavGraph(navHostController, context,authViewModel)
        composable(Graph.HOME) {
            HomeScreen(user = authViewModel.user)
        }
    }
}