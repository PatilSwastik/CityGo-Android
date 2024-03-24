package com.project.spass.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.spass.presentation.graphs.root_graph.RootNavigationGraph
import com.project.spass.presentation.ui.theme.SPass
import dagger.hilt.android.AndroidEntryPoint
import com.project.spass.presentation.screens.home_screen.component.HomeScreen
import com.project.spass.presentation.screens.sign_in_screen.AuthViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SPass {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    navHostController = rememberNavController()
                    RootNavigationGraph(navHostController = navHostController, context = LocalContext.current,authViewModel = authViewModel)
                    CheckAuthState()
                }
            }
        }
    }
    @Composable
    private fun CheckAuthState() {
        if(authViewModel.isUserAuthenticated) {
            HomeScreen(user = authViewModel.user)
        }
    }
}
