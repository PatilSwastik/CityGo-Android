package com.project.spass.presentation.screens.home_screen.component

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.spass.presentation.dashboard_screen.component.AppBar
import com.project.spass.presentation.graphs.detail_graph.DetailScreen
import com.project.spass.presentation.graphs.home_graph.HomeNavGraph


@SuppressLint("RememberReturnType")
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    boxScrollState: ScrollState = rememberScrollState(),
) {
    var searchText by remember {
        mutableStateOf("")
    }
    //topBar visibility state
    val topBarVisibilityState = remember {
        mutableStateOf(true)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                navController = navController,
                isVisible = topBarVisibilityState.value,
                searchCharSequence = {
                    searchText = it
                },
                onNotificationIconClick = {
                    navController.navigate(DetailScreen.NotificationScreen.route)
                })
        },
        bottomBar = {
            NavigationBar(navController = navController) { isVisible ->
                topBarVisibilityState.value = isVisible
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(boxScrollState)
        ) {
            HomeNavGraph(navHostController = navController, searchText = searchText)
        }
    }
}