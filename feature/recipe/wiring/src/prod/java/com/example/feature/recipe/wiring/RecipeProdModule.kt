package com.example.feature.recipe.wiring

import com.example.core.network.TokenProvider
import com.example.core.network.di.AuthInterceptor
import com.example.feature.recipe.api.data.RecipeRepository
import com.example.feature.recipe.impl.data.ApiRecipeMapper
import com.example.feature.recipe.impl.data.repository.RecipeRepositoryImpl
import com.example.feature.recipe.impl.remote.RecipeApi
import com.example.feature.recipe.wiring.di.RecipeBaseUrl
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeProdModule {

    @Provides
    @Singleton
    @RecipeBaseUrl
    fun provideRecipeBaseUrl(): String = "https://dummyjson.com/"

    @Provides
    @Singleton
    fun provideRecipeApi(
        @Recipe retrofit: Retrofit,
    ): RecipeApi {
        return retrofit.create(RecipeApi::class.java)
    }

    @Provides
    @Recipe
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            },
        )
        .build()

    @Provides
    @Recipe
    fun provideRetrofit(
        client: OkHttpClient,
        moshi: Moshi,
        @RecipeBaseUrl baseUrl: String,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(
        api: RecipeApi,
        mapper: ApiRecipeMapper,
    ): RecipeRepository = RecipeRepositoryImpl(api, mapper)

    @Provides
    fun provideTokenProvider(): TokenProvider {
        return object : TokenProvider {
            override fun getHeaderKey(): String {
                return "Authorization"
            }

            override fun getToken(): String {
                return "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            }
        }
    }
}
