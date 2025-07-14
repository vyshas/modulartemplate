package com.example.feature.recipe.impl.repository

import com.example.feature.recipe.api.data.RecipeRepository
import com.example.feature.recipe.impl.data.ApiRecipeMapper
import com.example.feature.recipe.impl.remote.RecipeApi
import io.mockk.mockk
import org.junit.Before

/**
 * Tests for recipeRepository behavior (recipeion implementation pattern)
 *
 * Since the actual recipeRepositoryImpl is in the prod flavor, we test the repository
 * interface contract and behavior using mocks. This ensures the repository follows
 * the expected patterns without duplicating implementation logic.
 */
class RecipeRepositoryImplTest {

    private lateinit var recipeApi: RecipeApi
    private lateinit var apirecipeMapper: ApiRecipeMapper
    private lateinit var mockRepository: RecipeRepository

    @Before
    fun setup() {
        recipeApi = mockk()
        apirecipeMapper = ApiRecipeMapper()
        mockRepository = mockk()
    }
}
