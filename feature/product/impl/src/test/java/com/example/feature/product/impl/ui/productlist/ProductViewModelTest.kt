package com.example.feature.product.impl.ui.productlist

import app.cash.turbine.test
import com.example.core.domain.DomainResult
import com.example.feature.product.api.domain.model.Product
import com.example.feature.product.api.domain.model.Rating
import com.example.feature.product.api.domain.usecase.GetProductsUseCase
import com.example.testutils.TestDispatcherProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductViewModelTest {

    private lateinit var getProductsUseCase: GetProductsUseCase
    private lateinit var productViewModel: ProductViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val dispatcherProvider = TestDispatcherProvider(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getProductsUseCase = mockk()
        productViewModel = ProductViewModel(getProductsUseCase, dispatcherProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState is Loading`() = runTest(testDispatcher) {
        // Given
        coEvery { getProductsUseCase.getProducts() } returns DomainResult.Success(emptyList())

        // When & Then
        productViewModel.uiState.test {
            assertTrue(awaitItem() is ProductUiState.Loading)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `uiState transitions to Success when use case returns success`() = runTest(testDispatcher) {
        // Given
        val products = listOf(
            Product(
                id = 1,
                title = "Test Product",
                price = 29.99,
                description = "Test description",
                category = "Test category",
                image = "test.jpg",
                rating = Rating(4.5, 10),
            ),
        )
        coEvery { getProductsUseCase.getProducts() } returns DomainResult.Success(products)

        // When & Then
        productViewModel.uiState.test {
            assertTrue(awaitItem() is ProductUiState.Loading) // Initial loading state
            advanceUntilIdle() // Allow coroutines to complete
            val successState = awaitItem()
            assertTrue(successState is ProductUiState.Success)
            assertEquals(products, (successState as ProductUiState.Success).products)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `uiState transitions to Error when use case returns error`() = runTest(testDispatcher) {
        // Given
        val errorMessage = "Failed to fetch products"
        coEvery { getProductsUseCase.getProducts() } returns DomainResult.Error(errorMessage)

        // When & Then
        productViewModel.uiState.test {
            assertTrue(awaitItem() is ProductUiState.Loading) // Initial loading state
            advanceUntilIdle()
            val errorState = awaitItem()
            assertTrue(errorState is ProductUiState.Error)
            assertEquals(errorMessage, (errorState as ProductUiState.Error).message)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `uiState transitions to Error when use case returns network error`() =
        runTest(testDispatcher) {
            // Given
            coEvery { getProductsUseCase.getProducts() } returns DomainResult.NetworkError

            // When & Then
            productViewModel.uiState.test {
                assertTrue(awaitItem() is ProductUiState.Loading) // Initial loading state
                advanceUntilIdle()
                val errorState = awaitItem()
                assertTrue(errorState is ProductUiState.Error)
                assertEquals("Network error", (errorState as ProductUiState.Error).message)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `FetchProducts intent triggers refresh`() = runTest(testDispatcher) {
        // Given
        val products1 = listOf(
            Product(1, "Product 1", 10.0, "Desc 1", "Cat 1", "img1.jpg", Rating(4.0, 5)),
        )
        val products2 = listOf(
            Product(2, "Product 2", 20.0, "Desc 2", "Cat 2", "img2.jpg", Rating(4.5, 10)),
        )

        coEvery { getProductsUseCase.getProducts() } returnsMany listOf(
            DomainResult.Success(products1),
            DomainResult.Success(products2),
        )

        productViewModel.uiState.test {
            // Initial fetch
            assertTrue(awaitItem() is ProductUiState.Loading)
            advanceUntilIdle()
            assertEquals(products1, (awaitItem() as ProductUiState.Success).products)

            // Trigger refresh
            productViewModel.onIntent(ProductUiIntent.FetchProducts)
            advanceUntilIdle()

            // Then
            assertTrue(awaitItem() is ProductUiState.Loading) // Loading again after refresh
            assertEquals(products2, (awaitItem() as ProductUiState.Success).products)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `ProductClicked intent emits NavigateToProductDetail effect`() = runTest(testDispatcher) {
        // Given
        val productId = 123
        coEvery { getProductsUseCase.getProducts() } returns DomainResult.Success(emptyList())

        // When & Then
        productViewModel.uiEffect.test {
            productViewModel.onIntent(ProductUiIntent.ProductClicked(productId))
            advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is ProductUiEffect.NavigateToProductDetail)
            assertEquals(productId, (effect as ProductUiEffect.NavigateToProductDetail).productId)
            cancelAndConsumeRemainingEvents()
        }
    }
}
