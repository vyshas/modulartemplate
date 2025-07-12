package com.example.feature.product.impl.ui.productlist

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feature.product.api.domain.model.Product
import com.example.feature.product.api.domain.model.Rating

@Composable
fun ProductScreen(
    viewModel: ProductViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect {
            when (it) {
                is ProductUiEffect.ShowToast -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is ProductUiEffect.NavigateToProductDetail -> {
                    // TODO: Implement actual navigation using ProductNavigator
                    Toast.makeText(context, "Navigate to product detail for ID: ${it.productId}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    ProductScreenContent(
        uiState = uiState,
        onItemClick = { product -> viewModel.onIntent(ProductUiIntent.ProductClicked(product.id)) },
        onRefresh = { viewModel.onIntent(ProductUiIntent.FetchProducts) }
    )
}

@Composable
fun ProductScreenContent(
    uiState: ProductUiState,
    onItemClick: (Product) -> Unit,
    onRefresh: () -> Unit = {}
) {
    when (uiState) {
        is ProductUiState.Error -> {
            ErrorState(
                message = uiState.message,
                onRetry = onRefresh
            )
        }

        is ProductUiState.Loading -> {
            LoadingState()
        }

        is ProductUiState.Success -> {
            SuccessState(
                products = uiState.products,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = message,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                modifier = Modifier.padding(horizontal = 32.dp)
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
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun EmptyState(onRefresh: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No products available",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRefresh) {
                Text("Refresh")
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        elevation = 2.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.title,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun SuccessState(
    products: List<Product>,
    onItemClick: (Product) -> Unit
) {
    if (products.isEmpty()) {
        EmptyState(onRefresh = { /* TODO: Handle refresh for empty state */ })
    } else {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(products) { product ->
                ProductItem(
                    product = product,
                    onClick = { onItemClick(product) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductScreenPreviewSuccess() {
    val mockProducts = listOf(
        Product(
            1,
            "Product 1",
            10.0,
            "Desc 1",
            "Cat 1",
            "",
            Rating(4.0, 100)
        ),
        Product(
            2,
            "Product 2",
            20.0,
            "Desc 2",
            "Cat 2",
            "",
            Rating(3.5, 50)
        )
    )
    Surface {
        ProductScreenContent(
            uiState = ProductUiState.Success(mockProducts),
            onItemClick = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductScreenPreviewEmpty() {
    Surface {
        ProductScreenContent(
            uiState = ProductUiState.Success(emptyList()),
            onItemClick = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductScreenPreviewLoading() {
    Surface {
        ProductScreenContent(
            uiState = ProductUiState.Loading,
            onItemClick = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductScreenPreviewError() {
    Surface {
        ProductScreenContent(
            uiState = ProductUiState.Error("Something went wrong. Please check your internet connection."),
            onItemClick = {},
            onRefresh = {}
        )
    }
}