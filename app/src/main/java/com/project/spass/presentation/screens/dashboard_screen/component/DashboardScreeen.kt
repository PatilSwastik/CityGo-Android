package com.project.spass.presentation.screens.dashboard_screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.spass.presentation.screens.dashboard_screen.DashboardViewModel
import com.project.spass.presentation.screens.dashboard_screen.PassState
import com.project.spass.presentation.ui.theme.PrimaryColor

@Composable
fun DashboardScreen(
    productViewModel: DashboardViewModel = hiltViewModel(),
    searchText : String,
    onItemClick: (Int) -> Unit,
) {
    val state = productViewModel.state.value
    val lightBlue = Color(0xFFC8D2E9)

    val filteredState = if (searchText.isEmpty()) state else PassState(pass = state.pass!!.filter {
         it.source.contains(searchText, ignoreCase = true) || it.destination.contains(searchText, ignoreCase = true)
     })
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(700.dp)
            .padding(start = 15.dp, end = 15.dp)
            .background(color = lightBlue, shape = RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(10.dp)
        ) {
            items(filteredState.pass?.size!!) {
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(24.dp))
                        .background(color = Color.White)
                        .height(140.dp)
                        .fillMaxWidth()
                        .clickable {
                            onItemClick(filteredState.pass[it].id)
                        },
                ){
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            androidx.compose.material3.Text(
                                text = "₹${filteredState.pass[it].price}",
                                fontWeight = FontWeight.Bold,
                            )
                            androidx.compose.material3.Text(
                                text = filteredState.pass[it].source,
                                fontWeight = FontWeight.Bold,
                            )
                            androidx.compose.material3.Text(
                                text = filteredState.pass[it].destination,
                                fontWeight = FontWeight.Bold,
                            )
                            Icon(
                                Icons.Rounded.ArrowForward,
                                contentDescription = null,
                                tint = Color.Black
                            )

                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.padding(8.dp))
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.Text(
                                text = "30 days",
                                textAlign = TextAlign.Left,
                            )
                            androidx.compose.material3.Text(
                                text = "View details",
                                color = Color(0xFF6200EE),
                                textAlign = TextAlign.Right,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}