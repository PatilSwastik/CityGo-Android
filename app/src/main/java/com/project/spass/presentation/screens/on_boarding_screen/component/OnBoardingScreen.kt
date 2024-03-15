package com.project.spass.presentation.screens.on_boarding_screen.component

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.FirebaseAuth
import com.project.spass.R
import com.project.spass.presentation.common.component.CustomDefaultBtn
import com.project.spass.presentation.graphs.auth_graph.AuthScreen
import com.project.spass.presentation.ui.theme.PrimaryColor
import com.project.spass.presentation.ui.theme.TextColor

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    // Check if shared preferences has a value
    val sharedPref = context.getSharedPreferences("spass", Context.MODE_PRIVATE)

    // Check firebase auth if user is logged in
     val auth = FirebaseAuth.getInstance()
     val user = auth.currentUser

    if(user != null){
        navController.navigate(AuthScreen.SignInSuccess.route)
    }

    val isFirstTime = sharedPref.getBoolean("isFirstTime", true)

    if(!isFirstTime){
        navController.navigate(AuthScreen.SignInScreen.route)
    }

    val splashImageList = listOf(
        R.drawable.splash_1,
        R.drawable.splash_2,
        R.drawable.splash_3,
    )
    val currentPosition = remember { mutableIntStateOf(0) }
    val animate = remember { mutableStateOf(true) }
    val pagerState = rememberPagerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        AnimatedContent(
            targetState = animate.value,
            modifier = Modifier
                .fillMaxWidth(),
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { value ->
                        value
                    }
                ) with slideOutHorizontally(
                    targetOffsetX = { value ->
                        -value
                    }
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                        .padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "SPASS",
                        fontSize = 50.sp,
                        color = MaterialTheme.colors.PrimaryColor,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.muli_bold)),
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    when (currentPosition.value) {
                        0 -> {
                            Text(
                                text = buildAnnotatedString {
                                    append(text = "Welcome to ")
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colors.TextColor,
                                        )
                                    ) {
                                        append("SPASS.")
                                    }
                                    append(" Let's Shop!")
                                },
                                color = MaterialTheme.colors.TextColor,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.muli)),
                            )
                        }
                        1 -> {
                            Text(
                                text = "We help people connect with store\naround Bangladesh",
                                color = MaterialTheme.colors.TextColor,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        else -> {
                            Text(
                                text = "We show easy way to shop.\nJust stay at home with us",
                                color = MaterialTheme.colors.TextColor,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            )

                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))

                    Image(
                        painter = painterResource(id = splashImageList[currentPosition.value]),
                        contentDescription = "Splash Image",
                        modifier = Modifier.padding(40.dp)
                    )
                }
            }
        )


        DotIndicator(splashImageList.size, currentPosition.intValue)

        CustomDefaultBtn(btnText = "Continue", shapeSize = 10f) {
            if (currentPosition.intValue < 2) {
                currentPosition.intValue++
                animate.value = !animate.value
            } else {
                navController.navigate(AuthScreen.SignInScreen.route)
            }
        }
    }
}