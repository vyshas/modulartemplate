package com.example.feature.home.impl.domain

import com.example.core.domain.DomainResult
import com.example.feature.home.api.data.HomeRepository
import com.example.feature.home.api.domain.model.HomeItem
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetHomeItemByIdUseCaseImplTest {

    private lateinit var homeRepository: HomeRepository
    private lateinit var getHomeItemByIdUseCase: GetHomeItemByIdUseCaseImpl

    @Before
    fun setup() {
        homeRepository = mockk()
        getHomeItemByIdUseCase = GetHomeItemByIdUseCaseImpl(homeRepository)
    }

    @Test
    fun `invoke returns success when item is found in cache`() = runBlocking {
        // Given
        val itemId = 1
        val cachedItem = HomeItem(id = itemId, title = "Cached Item")
        coEvery { homeRepository.getCachedItemById(itemId) } returns cachedItem

        // When
        val result = getHomeItemByIdUseCase.invoke(itemId)

        // Then
        assertTrue(result is DomainResult.Success)
        assertEquals(cachedItem, (result as DomainResult.Success).data)
    }

    @Test
    fun `invoke returns error when item is not found in cache`() = runBlocking {
        // Given
        val itemId = 99
        coEvery { homeRepository.getCachedItemById(itemId) } returns null

        // When
        val result = getHomeItemByIdUseCase.invoke(itemId)

        // Then
        assertTrue(result is DomainResult.Error)
        assertEquals("Item not found", (result as DomainResult.Error).message)
    }
}
