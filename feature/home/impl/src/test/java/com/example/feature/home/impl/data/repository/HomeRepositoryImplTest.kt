package com.example.feature.home.impl.data.repository

import com.example.core.domain.DomainResult
import com.example.feature.home.api.domain.model.HomeItem
import com.example.feature.home.impl.data.api.HomeApi
import com.example.feature.home.impl.data.model.ApiHomeItem
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HomeRepositoryImplTest {

    private lateinit var homeApi: HomeApi
    private lateinit var homeRepository: HomeRepositoryImpl

    @Before
    fun setup() {
        homeApi = mockk()
        homeRepository = HomeRepositoryImpl(homeApi)
    }

    @Test
    fun `getHomeItems returns success when API call is successful`() = runBlocking {
        // Given
        val apiHomeItems = listOf(
            ApiHomeItem(id = 1, title = "Item 1"),
            ApiHomeItem(id = 2, title = "Item 2"),
        )
        coEvery { homeApi.fetchHomeItems() } returns apiHomeItems

        // When
        val result = homeRepository.getHomeItems()

        // Then
        assertTrue(result is DomainResult.Success)
        assertEquals(2, (result as DomainResult.Success).data.size)
        assertEquals("Item 1", result.data[0].title)
    }

    @Test
    fun `getHomeItems returns error when API call fails`() = runBlocking {
        // Given
        val errorMessage = "Network error"
        coEvery { homeApi.fetchHomeItems() } throws Exception(errorMessage)

        // When
        val result = homeRepository.getHomeItems()

        // Then
        assertTrue(result is DomainResult.Error)
        assertEquals("Failed to fetch items: $errorMessage", (result as DomainResult.Error).message)
    }

    @Test
    fun `cacheItems correctly caches items`() = runBlocking {
        // Given
        val homeItems = listOf(
            HomeItem(id = 1, title = "Cached Item 1"),
            HomeItem(id = 2, title = "Cached Item 2"),
        )

        // When
        homeRepository.cacheItems(homeItems)

        // Then
        assertEquals("Cached Item 1", homeRepository.getCachedItemById(1)?.title)
        assertEquals("Cached Item 2", homeRepository.getCachedItemById(2)?.title)
    }

    @Test
    fun `getCachedItemById returns null for non-existent item`() = runBlocking {
        // When
        val result = homeRepository.getCachedItemById(99)

        // Then
        assertEquals(null, result)
    }
}
