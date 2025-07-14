package com.example.feature.recipe.wiring.di

import com.example.core.navigation.FeatureEntry
import com.example.feature.recipe.api.RecipeEntry
import com.example.feature.recipe.api.RecipeNavigator
import com.example.feature.recipe.api.domain.usecase.GetRecipesUseCase
import com.example.feature.recipe.impl.RecipeEntryImpl
import com.example.feature.recipe.impl.data.ApiRecipeMapper
import com.example.feature.recipe.impl.domain.usecase.GetRecipesUseCaseImpl
import com.example.feature.recipe.impl.navigation.RecipeNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RecipeModule {

    @Binds
    @Singleton
    abstract fun bindGetRecipesUseCase(
        impl: GetRecipesUseCaseImpl,
    ): GetRecipesUseCase

    @Binds
    @IntoSet
    abstract fun bindRecipeEntry(
        recipeEntryImpl: RecipeEntryImpl,
    ): FeatureEntry

    @Binds
    @Singleton
    abstract fun bindRecipeNavigator(
        impl: RecipeNavigatorImpl,
    ): RecipeNavigator

    @Binds
    @Singleton
    abstract fun bindRecipeEntryAsRecipeEntry(
        impl: RecipeEntryImpl,
    ): RecipeEntry

    companion object Companion {
        @Provides
        @Singleton
        fun provideApirecipeMapper(): ApiRecipeMapper = ApiRecipeMapper()
    }
}
