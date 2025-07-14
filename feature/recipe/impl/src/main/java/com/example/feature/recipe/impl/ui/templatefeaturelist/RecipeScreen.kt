package com.example.feature.recipe.impl.ui.recipelist

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
import com.example.feature.recipe.api.domain.model.Recipe

@Composable
fun RecipeScreen(
    viewModel: RecipeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect {
            when (it) {
                is RecipeUiEffect.ShowToast -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

                is RecipeUiEffect.NavigateToRecipeDetail -> {
                    // TODO: Implement actual navigation using recipeNavigator
                    Toast.makeText(
                        context,
                        "Navigate to recipe detail for ID: ${it.recipeId}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    RecipeScreenContent(
        uiState = uiState,
        onItemClick = { recipe ->
            viewModel.onIntent(
                RecipeUiIntent.recipeClicked(recipe.id)
            )
        },
        onRefresh = { viewModel.onIntent(RecipeUiIntent.Fetchrecipes) }
    )
}

@Composable
fun RecipeScreenContent(
    uiState: RecipeUiState,
    onItemClick: (Recipe) -> Unit,
    onRefresh: () -> Unit = {}
) {
    when (uiState) {
        is RecipeUiState.Error -> {
            ErrorState(
                message = uiState.message,
                onRetry = onRefresh
            )
        }

        is RecipeUiState.Loading -> {
            LoadingState()
        }

        is RecipeUiState.Success -> {
            SuccessState(
                recipes = uiState.Recipes,
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
                text = "No recipes available",
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
fun RecipeItem(recipe: Recipe, onClick: () -> Unit) {
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
                text = recipe.name,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$${recipe.caloriesPerServing}",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun SuccessState(
    recipes: List<Recipe>,
    onItemClick: (Recipe) -> Unit
) {
    if (recipes.isEmpty()) {
        EmptyState(onRefresh = { /* TODO: Handle refresh for empty state */ })
    } else {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(recipes) { recipe ->
                RecipeItem(
                    recipe = recipe,
                    onClick = { onItemClick(recipe) }
                )
            }
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun recipeScreenPreviewSuccess() {
    val mockrecipes = listOf(
        Recipe(
            caloriesPerServing = 1,
            cookTimeMinutes = "recipe 1",
            cuisine = 10.0,
            difficulty = "Desc 1",
            id = "Cat 1",
            ingredients = "",
            instructions =
        ),
        Recipe(
            2,
            "recipe 2",
            20.0,
            "Desc 2",
            "Cat 2",
            "",
            Rating(3.5, 50)
        )
    )
    Surface {
        recipeScreenContent(
            uiState = recipeUiState.Success(mockrecipes),
            onItemClick = {},
            onRefresh = {}
        )
    }
}*/

@Preview(showBackground = true)
@Composable
fun RecipeScreenPreviewEmpty() {
    Surface {
        RecipeScreenContent(
            uiState = RecipeUiState.Success(emptyList()),
            onItemClick = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeScreenPreviewLoading() {
    Surface {
        RecipeScreenContent(
            uiState = RecipeUiState.Loading,
            onItemClick = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeScreenPreviewError() {
    Surface {
        RecipeScreenContent(
            uiState = RecipeUiState.Error("Something went wrong. Please check your internet connection."),
            onItemClick = {},
            onRefresh = {}
        )
    }
}