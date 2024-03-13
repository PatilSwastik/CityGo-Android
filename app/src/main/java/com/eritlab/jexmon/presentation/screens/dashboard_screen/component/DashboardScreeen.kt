package com.eritlab.jexmon.presentation.screens.dashboard_screen.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.toColor
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.eritlab.jexmon.R
import com.eritlab.jexmon.presentation.graphs.Graph
import com.eritlab.jexmon.presentation.graphs.detail_graph.DetailScreen
import com.eritlab.jexmon.presentation.screens.dashboard_screen.DashboardViewModel
import com.eritlab.jexmon.presentation.ui.theme.PrimaryColor
import com.eritlab.jexmon.presentation.ui.theme.PrimaryLightColor
import com.eritlab.jexmon.presentation.ui.theme.SecondaryColor
import com.eritlab.jexmon.presentation.ui.theme.TextColor
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun DashboardScreen(
    productViewModel: DashboardViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit
) {
    val state = productViewModel.state.value
    Box(
        modifier = Modifier
            .width(350.dp)
            .height(700.dp)
            .padding(start = 15.dp, end = 15.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            items(state.product!!.size) {
                Box(
                    modifier = Modifier
                        .width(350.dp)
                        .height(150.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            onItemClick(state.product[it].id)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.product[it].title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(150.dp)
                    )
                    Row(
                        modifier = Modifier
                            .width(150.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Rs. ${state.product[it].price}",
                            fontWeight = FontWeight(600),
                            color = MaterialTheme.colors.PrimaryColor
                        )
                    }
                }
            }
        }
    }
}