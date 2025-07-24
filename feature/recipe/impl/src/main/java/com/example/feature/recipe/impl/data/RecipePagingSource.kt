package com.example.feature.recipe.impl.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.feature.recipe.api.domain.model.Recipe
import com.example.feature.recipe.impl.remote.RecipeApi
import javax.inject.Inject

class RecipePagingSource @Inject constructor(
    private val api: RecipeApi,
    private val mapper: ApiRecipeMapper,
    private val pageSize: Int
) : PagingSource<Int, Recipe>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> {
        val page = params.key ?: 1
        val skip = (page - 1) * pageSize
        return try {
            val response = api.getRecipes(limit = pageSize, skip = skip)
            val recipes = mapper.map(response)
            val totalPages = (response.total + pageSize - 1) / pageSize
            LoadResult.Page(
                data = recipes,
                prevKey = if (page > 1) page - 1 else null,
                nextKey = if (page < totalPages) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? {
        // Return closest page to anchor position
        return state.anchorPosition?.let { anchorPos ->
            val anchorPage = state.closestPageToPosition(anchorPos)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
