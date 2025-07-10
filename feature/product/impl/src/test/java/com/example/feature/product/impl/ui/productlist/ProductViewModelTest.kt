package com.example.feature.product.impl.ui.productlist

import com.example.feature.product.api.domain.model.Product
import com.example.feature.product.api.domain.model.Rating
import com.example.feature.product.api.domain.usecase.GetProductsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import com.example.core.domain.DomainResult
import app.cash.turbine.test

/**
 * Comprehensive tests for ProductViewModel
 *
 * Tests all UI intents, state transitions, and effect emissions.
 */
@ExperimentalCoroutinesApi
class ProductViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getProductsUseCase: GetProductsUseCase
    private lateinit var viewModel: ProductViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getProductsUseCase = mockk()
        viewModel = ProductViewModel(getProductsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading and products are fetched automatically`() = runTest {
        // Given
        val products = listOf(createTestProduct(1), createTestProduct(2))
        coEvery { getProductsUseCase.getProducts() } returns DomainResult.Success(products)

        // When & Then
        viewModel.uiState.test {
            assertEquals(ProductUiState.Loading, awaitItem())
            assertEquals(ProductUiState.Success(products), awaitItem())
        }

        // Verify use case was called automatically
        coVerify { getProductsUseCase.getProducts() }
    }

    @Test
    fun `fetchProducts success updates state with products`() = runTest {
        // Given
        val products = listOf(
            createTestProduct(1, "iPhone 14", 999.99),
            createTestProduct(2, "Samsung Galaxy S23", 899.99),
            createTestProduct(3, "Google Pixel 7", 599.99)
        )
        coEvery { getProductsUseCase.getProducts() } returns DomainResult.Success(products)

        // When & Then
        viewModel.uiState.test {
            assertEquals(ProductUiState.Loading, awaitItem())
            viewModel.onIntent(ProductUiIntent.FetchProducts)
            assertEquals(ProductUiState.Success(products), awaitItem())
        }

        // Verify use case was called twice (initial + manual fetch)
        coVerify(exactly = 2) { getProductsUseCase.getProducts() }
    }

    @Test
    fun `fetchProducts with empty list returns success with empty list`() = runTest {
        // Given
        val emptyProducts = emptyList<Product>()
        coEvery { getProductsUseCase.getProducts() } returns DomainResult.Success(emptyProducts)

        // When & Then
        viewModel.uiState.test {
            assertEquals(ProductUiState.Loading, awaitItem())
            viewModel.onIntent(ProductUiIntent.FetchProducts)
            assertEquals(ProductUiState.Success(emptyProducts), awaitItem())
        }
    }

    @Test
    fun `fetchProducts failure updates state with error message`() = runTest {
        // Given
        val errorMessage = "Failed to load products from server"
        coEvery { getProductsUseCase.getProducts() } returns DomainResult.Error(errorMessage)

        // When & Then
        viewModel.uiState.test {
            assertEquals(ProductUiState.Loading, awaitItem())
            viewModel.onIntent(ProductUiIntent.FetchProducts)
            assertEquals(ProductUiState.Error(errorMessage), awaitItem())
        }
    }

    @Test
    fun `fetchProducts network error updates state with network error message`() = runTest {
        // Given
        coEvery { getProductsUseCase.getProducts() } returns DomainResult.NetworkError

        // When & Then
        viewModel.uiState.test {
            assertEquals(ProductUiState.Loading, awaitItem())
            viewModel.onIntent(ProductUiIntent.FetchProducts)
            assertEquals(ProductUiState.Error("Network error"), awaitItem())
        }
    }

    @Test
    fun `onProductClicked sends NavigateToProductDetail effect`() = runTest {
        // Given
        val productId = 12345

        // When & Then
        viewModel.uiEffect.test {
            viewModel.onIntent(ProductUiIntent.ProductClicked(productId))
            assertEquals(ProductUiEffect.NavigateToProductDetail(productId), awaitItem())
        }
    }

    @Test
    fun `multiple product clicks emit multiple navigation effects`() = runTest {
        // Given
        val productIds = listOf(1, 2, 3)

        // When & Then
        viewModel.uiEffect.test {
            productIds.forEach { id ->
                viewModel.onIntent(ProductUiIntent.ProductClicked(id))
                assertEquals(ProductUiEffect.NavigateToProductDetail(id), awaitItem())
            }
        }
    }

    @Test
    fun `retry fetch products after error works correctly`() = runTest {
        // Given
        val errorMessage = "Network timeout"
        val products = listOf(createTestProduct(1), createTestProduct(2))

        // First call fails, second succeeds
        coEvery { getProductsUseCase.getProducts() } returnsMany listOf(
            DomainResult.Error(errorMessage),
            DomainResult.Success(products)
        )

        // When & Then
        viewModel.uiState.test {
            // Initial automatic fetch
            assertEquals(ProductUiState.Loading, awaitItem())
            assertEquals(ProductUiState.Error(errorMessage), awaitItem())

            // Manual retry
            viewModel.onIntent(ProductUiIntent.FetchProducts)
            assertEquals(ProductUiState.Loading, awaitItem())
            assertEquals(ProductUiState.Success(products), awaitItem())
        }

        // Verify use case was called twice
        coVerify(exactly = 2) { getProductsUseCase.getProducts() }
    }

    @Test
    fun `concurrent intents are handled correctly`() = runTest {
        // Given
        val products = listOf(createTestProduct(1))
        coEvery { getProductsUseCase.getProducts() } returns DomainResult.Success(products)

        // When & Then
        viewModel.uiState.test {
            assertEquals(ProductUiState.Loading, awaitItem())

            // Send multiple fetch intents quickly
            repeat(3) {
                viewModel.onIntent(ProductUiIntent.FetchProducts)
            }

            // Should only get the final result
            assertEquals(ProductUiState.Success(products), awaitItem())
        }
    }

    @Test
    fun `state changes correctly from success to loading to success`() = runTest {
        // Given
        val firstProducts = listOf(createTestProduct(1))
        val secondProducts = listOf(createTestProduct(2), createTestProduct(3))

        coEvery { getProductsUseCase.getProducts() } returnsMany listOf(
            DomainResult.Success(firstProducts),
            DomainResult.Success(secondProducts)
        )

        // When & Then
        viewModel.uiState.test {
            // Initial load
            assertEquals(ProductUiState.Loading, awaitItem())
            assertEquals(ProductUiState.Success(firstProducts), awaitItem())

            // Refresh
            viewModel.onIntent(ProductUiIntent.FetchProducts)
            assertEquals(ProductUiState.Loading, awaitItem())
            assertEquals(ProductUiState.Success(secondProducts), awaitItem())
        }
    }

    // Helper function to create test products
    private fun createTestProduct(
        id: Int,
        title: String = "Test Product $id",
        price: Double = 10.0 * id
    ): Product {
        return Product(
            id = id,
            title = title,
            price = price,
            description = "Test description for product $id",
            category = "test-category",
            image = "https://test.com/image$id.jpg",
            rating = Rating(rate = 4.0 + (id * 0.1), count = 100 + (id * 10))
        )
    }
}