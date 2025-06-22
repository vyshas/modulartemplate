package com.example.feature.home.impl.ui.homelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feature.home.api.domain.model.HomeItem

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onItemClick: (HomeItem) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreenContent(uiState, onItemClick)
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onItemClick: (HomeItem) -> Unit
) {
    when (uiState) {
        is HomeUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = uiState.message, color = Color.Red)
            }
        }

        is HomeUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF2196F3) // Material Blue 500
                )
            }
        }

        is HomeUiState.Success -> {
            LazyColumn {
                items(uiState.items) { item ->
                    HomeListItem(item, onClick = { onItemClick(item) })
                }
            }
        }
    }
}

@Composable
fun HomeListItem(item: HomeItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "ID: ${item.id}",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreviewSuccess() {
    val mockItems = listOf(
        HomeItem(1, "Preview Item 1"),
        HomeItem(2, "Preview Item 2"),
        HomeItem(3, "Preview Item 3")
    )
    Surface {
        HomeScreenContent(uiState = HomeUiState.Success(mockItems), onItemClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreviewLoading() {
    Surface {
        HomeScreenContent(uiState = HomeUiState.Loading, onItemClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreviewError() {
    Surface {
        HomeScreenContent(uiState = HomeUiState.Error("Something went wrong"), onItemClick = {})
    }
}
