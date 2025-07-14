package com.example.feature.recipe.wiring

import android.content.Context
import com.example.feature.recipe.api.data.RecipeRepository
import com.example.feature.recipe.impl.data.ApiRecipeMapper
import com.example.feature.recipe.impl.data.repository.RecipeRepositoryMockImpl
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeMockModule {

    @Provides
    @Singleton
    fun provideRecipeRepository(
        @ApplicationContext context: Context,
        mapper: ApiRecipeMapper,
        moshi: Moshi
    ): RecipeRepository {
        // Ensure we use application context, which is properly injected by Hilt
        return RecipeRepositoryMockImpl(context.applicationContext, mapper, moshi)
    }
}