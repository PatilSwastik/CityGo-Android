package com.project.spass.presentation.graphs

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.project.spass.presentation.graphs.auth_graph.AuthScreen
import com.project.spass.presentation.screens.forget_password_screen.component.ForgetPasswordScreen
import com.project.spass.presentation.screens.on_boarding_screen.component.SplashScreen
import com.project.spass.presentation.screens.otp_screen.component.OTPScreen
import com.project.spass.presentation.screens.pass_screen.compnent.PassScreen
import com.project.spass.presentation.screens.sign_in_screen.AuthViewModel
import com.project.spass.presentation.screens.sign_in_screen.component.LoginScreen
import com.project.spass.presentation.screens.sign_success_screen.component.SignInScreen
import com.project.spass.presentation.screens.sign_up_screen.component.SignUpScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi


@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
fun NavGraphBuilder.authNavGraph(navController: NavHostController, context: Context,authViewModel : AuthViewModel) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.SignInScreen.route
    ) {
        composable(AuthScreen.OnBoardingScreen.route) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                (context as Activity).window.decorView.windowInsetsController?.hide(
                    WindowInsets.Type.statusBars()
                );
            } else {
                (context as Activity).window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            SplashScreen(navController)

            Log.d("Navigation Call", "Called Splash Screen")
        }
        composable(AuthScreen.SignInScreen.route) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                (context as Activity).window.decorView.windowInsetsController?.show(
                    WindowInsets.Type.statusBars()
                );
            } else {
                (context as Activity).window.apply {
                    clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                }

            }
            LoginScreen(navController = navController,authViewModel = authViewModel)
        }
        composable(AuthScreen.ForgetPasswordScreen.route) {
            ForgetPasswordScreen(navController = navController)
        }
        composable(AuthScreen.OTPScreen.route) {
            OTPScreen(navController = navController)
        }
        composable(AuthScreen.SignUpScreen.route) {
            SignUpScreen(navController = navController)
        }
        composable(AuthScreen.SignInSuccess.route) {
            SignInScreen(navController = navController)
        }
    }
}