package com.example.feature.order.impl.ui.orderlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feature.home.api.HomeDestination

@Composable
fun OrderScreen(
    viewModel: OrderViewModel,
    onNavigate: (HomeDestination) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is OrderUiEffect.NavigateToHomeDetail -> {
                    onNavigate(HomeDestination.Detail(effect.homeId))
                }
            }
        }
    }

    OrderScreenContent(
        uiState = uiState,
        onViewHomeClick = {
            viewModel.onIntent(OrderUiIntent.OnViewHomeDetailClicked)
        }
    )
}

@Composable
fun OrderScreenContent(
    uiState: OrderUiState,
    onViewHomeClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Order Screen", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            uiState.order?.let { order ->
                Text("Order ID: ${order.orderId}")
                Text("Amount: ${order.amount}")
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onViewHomeClick) {
                    Text("View Home Details")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderScreenContentPreview() {
    MaterialTheme {
        OrderScreenContent(
            uiState = OrderUiState(
                order = com.example.feature.order.api.domain.model.Order(1, 99.99),
                isLoading = false
            ),
            onViewHomeClick = {}
        )
    }
}


