package com.example.feature.home.impl.domain

import com.example.core.domain.DomainResult
import com.example.feature.home.api.data.HomeRepository
import com.example.feature.home.api.domain.model.HomeItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetHomeItemsUseCaseImplTest {
    private lateinit var homeRepository: HomeRepository
    private lateinit var getHomeItemsUseCase: GetHomeItemsUseCaseImpl

    @Before
    fun setup() {
        homeRepository = mockk()
        getHomeItemsUseCase = GetHomeItemsUseCaseImpl(homeRepository)
    }

    @Test
    fun `getHomeItems returns success and caches items when repository call is successful`() =
        runBlocking {
            // Given
            val homeItems = listOf(
                HomeItem(id = 1, title = "Item 1"),
                HomeItem(id = 2, title = "Item 2")
            )
            coEvery { homeRepository.getHomeItems() } returns DomainResult.Success(homeItems)
            coEvery { homeRepository.cacheItems(any()) } returns Unit // Mock cacheItems

            // When
            val result = getHomeItemsUseCase.getHomeItems()

            // Then
            assertTrue(result is DomainResult.Success)
            assertEquals(2, (result as DomainResult.Success).data.size)
            assertEquals("Item 1", result.data[0].title)
            coVerify(exactly = 1) { homeRepository.cacheItems(homeItems) }
        }

    @Test
    fun `getHomeItems returns error and does not cache items when repository call fails`() =
        runBlocking {
            // Given
            val errorMessage = "Repository error"
            coEvery { homeRepository.getHomeItems() } returns DomainResult.Error(errorMessage)

            // When
            val result = getHomeItemsUseCase.getHomeItems()

            // Then
            assertTrue(result is DomainResult.Error)
            assertEquals(errorMessage, (result as DomainResult.Error).message)
            coVerify(exactly = 0) { homeRepository.cacheItems(any()) } // Ensure cacheItems is not called
        }
}