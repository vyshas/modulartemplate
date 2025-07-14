package com.example.feature.home.impl.ui.homedetail

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feature.home.api.domain.model.HomeItem

@Composable
fun HomeDetailScreen(
    viewModel: HomeDetailViewModel,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is HomeDetailUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (uiState) {
                            is HomeDetailUiState.Success ->
                                (uiState as HomeDetailUiState.Success).item.title

                            else -> "Details"
                        },
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                actions = {
                    if (uiState is HomeDetailUiState.Error) {
                        IconButton(
                            onClick = {
                                viewModel.retry()
                            },
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Retry",
                            )
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            HomeDetailScreenContent(
                uiState = uiState,
                onRetry = {
                    viewModel.retry()
                },
            )
        }
    }
}

@Composable
fun HomeDetailScreenContent(
    uiState: HomeDetailUiState,
    onRetry: () -> Unit = {},
) {
    when (uiState) {
        is HomeDetailUiState.Success -> {
            SuccessContent(item = uiState.item)
        }

        is HomeDetailUiState.Error -> {
            ErrorContent(
                message = uiState.message,
                onRetry = onRetry,
            )
        }

        HomeDetailUiState.Loading -> {
            LoadingContent()
        }
    }
}

@Composable
private fun SuccessContent(item: HomeItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp,
            shape = RoundedCornerShape(12.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
            ) {
                Text(
                    text = "Home Item Details",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                )

                Spacer(modifier = Modifier.height(16.dp))

                DetailRow(
                    label = "ID",
                    value = item.id.toString(),
                )

                Spacer(modifier = Modifier.height(12.dp))

                DetailRow(
                    label = "Title",
                    value = item.title,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Additional content sections can go here
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 2.dp,
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = "Additional Information",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Medium,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "This is where additional details about the home item would be displayed.",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                )
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            fontWeight = FontWeight.Medium,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Divider(
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
            thickness = 0.5.dp,
        )
    }
}

@Composable
private fun ErrorContent(
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
            modifier = Modifier.padding(32.dp),
        ) {
            Text(
                text = "Oops! Something went wrong",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onRetry,
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                Text("Try Again")
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Loading details...",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeDetailScreenPreviewSuccess() {
    val mockItem = HomeItem(
        id = 1,
        title = "Beautiful Modern Home",
    )
    MaterialTheme {
        HomeDetailScreenContent(
            uiState = HomeDetailUiState.Success(mockItem),
            onRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeDetailScreenPreviewError() {
    MaterialTheme {
        HomeDetailScreenContent(
            uiState = HomeDetailUiState.Error("Failed to load home details. Please check your internet connection."),
            onRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeDetailScreenPreviewLoading() {
    MaterialTheme {
        HomeDetailScreenContent(
            uiState = HomeDetailUiState.Loading,
            onRetry = {},
        )
    }
}
