package com.example.feature.recipe.impl.remote

import com.example.testutils.ApiAbstract
import com.example.testutils.MainCoroutinesRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class RecipeApiTest : ApiAbstract<RecipeApi>() {

    private lateinit var apiService: RecipeApi

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var coroutineRule = MainCoroutinesRule()

    @Before
    fun setUp() {
        apiService = createService(RecipeApi::class.java)
    }

    @Test
    fun `test getRecipes request path is correct`() = runBlocking {
        // Given
        enqueueResponse("/recipes.json")

        // When
        apiService.getRecipes()

        // Then
        assertRequestPath("/recipes?limit=10")

    }

    @Throws(IOException::class)
    @Test
    fun `test getRecipes returns list of recipes`() = runBlocking {
        // Given
        enqueueResponse("/recipes.json")

        // When
        val response = apiService.getRecipes()
        mockWebServer.takeRequest()

        // Then
        assertThat(response.recipes.size, `is`(10))
        assertThat(response.recipes[0].name, `is`("Classic Margherita Pizza"))
    }
}