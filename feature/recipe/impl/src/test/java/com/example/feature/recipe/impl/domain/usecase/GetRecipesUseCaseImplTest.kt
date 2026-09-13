package com.example.feature.recipe.impl.domain.usecase

import com.example.core.domain.DomainResult
import com.example.feature.recipe.api.data.RecipeRepository
import com.example.feature.recipe.api.domain.model.Recipe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetRecipesUseCaseImplTest {

    private val repository = mockk<RecipeRepository>()
    private val useCase = GetRecipesUseCaseImpl(repository)

    @Test
    fun `getrecipes delegates to repository and returns result`() = runTest {
        val mockRecipe = Recipe(
            id = 1,
            name = "Test Recipe",
            caloriesPerServing = 200,
            cookTimeMinutes = 30,
            cuisine = "Italian",
            difficulty = "Easy",
            ingredients = listOf("Pasta"),
            instructions = listOf("Cook"),
            mealType = listOf("Dinner")
        )
        val expectedResult = DomainResult.Success(listOf(mockRecipe))
        coEvery { repository.getRecipes() } returns expectedResult

        // Act
        val actualResult = useCase.getrecipes()

        // Assert
        assertEquals(expectedResult, actualResult)
        coVerify(exactly = 1) { repository.getRecipes() }
    }
}
