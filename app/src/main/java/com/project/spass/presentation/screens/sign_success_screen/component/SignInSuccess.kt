package com.project.spass.presentation.screens.sign_success_screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.spass.presentation.common.component.CustomDefaultBtn
import com.project.spass.presentation.ui.theme.TextColor
import com.project.spass.presentation.graphs.Graph
import com.project.spass.R
@Composable
fun SignInScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
        }
        Image(
            painter = painterResource(id = R.drawable.success),
            contentDescription = "Login Success Image"
        )
        Text(
            text = "Login Successful",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(50.dp))

        CustomDefaultBtn(shapeSize = 50f, btnText = "Continue") {
            navController.navigate(Graph.HOME)
        }
    }
}
