package com.example.feature.order.impl.ui.orderlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feature.home.api.HomeDestination


@Composable
fun OrderScreen(
    viewModel: OrderViewModel,
    onNavigate: (HomeDestination) -> Unit
) {
    val effectFlow = viewModel.uiEffect

    // Handle effects inside Compose
    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {
                is OrderUiEffect.NavigateToHomeDetail -> {
                    onNavigate(HomeDestination.Detail(effect.homeId))
                }
            }
        }
    }

    OrderScreenContent(
        onViewHomeClick = { homeId: Int ->
            viewModel.onIntent(
                OrderUiIntent.OnViewHomeDetailClicked(homeId)
            )
        }
    )
}


@Composable
fun OrderScreenContent(
    onViewHomeClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Order Screen", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { onViewHomeClick(7) }) {
            Text("View Home Details")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderScreenContentPreview() {
    MaterialTheme {
        OrderScreenContent(
            onViewHomeClick = {}
        )
    }
}


