package com.project.spass.presentation.screens.dashboard_screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

    val filteredState = if (searchText.isEmpty()) state else PassState(pass = state.pass!!.filter {
         it.source.contains(searchText, ignoreCase = true) || it.destination.contains(searchText, ignoreCase = true)
     })
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(700.dp)
            .padding(start = 15.dp, end = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            items(filteredState.pass?.size!!) {
                Box(
                    modifier = Modifier
                        .width(350.dp)
                        .height(150.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            onItemClick(filteredState.pass[it].id)
                        },
                ) {
                    Row (
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = filteredState.pass[it].id.toString(),
                                modifier = Modifier.width(150.dp)
                            )
                            Text(
                                text = filteredState.pass[it].source,
                                modifier = Modifier.width(150.dp)
                            )
                            Text(
                                text = filteredState.pass[it].destination,
                                modifier = Modifier.width(150.dp)
                            )
                        }
                        Text(
                            text = "Rs. ${filteredState.pass[it].price}",
                            fontWeight = FontWeight(600),
                            color = MaterialTheme.colors.PrimaryColor
                        )
                    }
                }
            }
        }
    }
}