package com.example.feature.home.impl.ui.homelist

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feature.home.api.domain.model.HomeItem

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onItemClick: (HomeItem) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(
        uiState = uiState,
        onItemClick = onItemClick,
        onRefresh = { viewModel.refresh() },
    )
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onItemClick: (HomeItem) -> Unit,
    onRefresh: () -> Unit = {},
) {
    when (uiState) {
        is HomeUiState.Error -> {
            ErrorState(
                message = uiState.message,
                onRetry = onRefresh,
            )
        }

        is HomeUiState.Loading -> {
            LoadingState()
        }

        is HomeUiState.Success -> {
            SuccessState(
                items = uiState.items,
                onItemClick = onItemClick,
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = message,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                modifier = Modifier.padding(horizontal = 32.dp),
            ) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            )
        }
    }
}

@Composable
private fun EmptyState(onRefresh: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "No items available",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRefresh) {
                Text("Refresh")
            }
        }
    }
}

@Composable
fun HomeListItem(item: HomeItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        elevation = 2.dp,
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "ID: ${item.id}",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            )
        }
    }
}

@Composable
private fun SuccessState(
    items: List<HomeItem>,
    onItemClick: (HomeItem) -> Unit,
) {
    if (items.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "No items available",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            )
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(items) { item ->
                HomeListItem(
                    item = item,
                    onClick = { onItemClick(item) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreviewSuccess() {
    val mockItems = listOf(
        HomeItem(1, "Preview Item 1"),
        HomeItem(2, "Preview Item 2"),
        HomeItem(3, "Preview Item 3"),
    )
    Surface {
        HomeScreenContent(
            uiState = HomeUiState.Success(mockItems),
            onItemClick = {},
            onRefresh = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreviewEmpty() {
    Surface {
        HomeScreenContent(
            uiState = HomeUiState.Success(emptyList()),
            onItemClick = {},
            onRefresh = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreviewLoading() {
    Surface {
        HomeScreenContent(
            uiState = HomeUiState.Loading,
            onItemClick = {},
            onRefresh = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreviewError() {
    Surface {
        HomeScreenContent(
            uiState = HomeUiState.Error("Something went wrong. Please check your internet connection."),
            onItemClick = {},
            onRefresh = {},
        )
    }
}
