package com.example.feature.recipe.impl.data.repository

import android.content.Context
import com.example.core.domain.DomainResult
import com.example.feature.recipe.api.data.RecipeRepository
import com.example.feature.recipe.api.domain.model.Recipe
import com.example.feature.recipe.impl.data.ApiRecipeListResponse
import com.example.feature.recipe.impl.data.ApiRecipeMapper
import com.squareup.moshi.Moshi
import java.io.InputStream
import javax.inject.Inject

class RecipeRepositoryMockImpl @Inject constructor(
    private val context: Context,
    private val mapper: ApiRecipeMapper,
    private val moshi: Moshi
) : RecipeRepository {

    override suspend fun getRecipes(): DomainResult<List<Recipe>> {
        return try {
            val json = readJsonFromAssets("recipe_mock.json")
            val adapter = moshi.adapter(ApiRecipeListResponse::class.java)
            val response: ApiRecipeListResponse? = adapter.fromJson(json)
            val recipes: List<Recipe> = response?.let { mapper.map(it) } ?: emptyList()
            DomainResult.Success(recipes)
        } catch (e: Exception) {
            DomainResult.Error("Failed to load mock recipes: ${e.message}")
        }
    }

    private fun readJsonFromAssets(fileName: String): String {
        return try {
            val inputStream: InputStream = context.assets.open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
