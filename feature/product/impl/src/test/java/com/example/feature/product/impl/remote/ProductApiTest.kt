package com.example.feature.product.impl.remote

import com.example.testutils.ApiAbstract
import com.example.testutils.MainCoroutinesRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class ProductApiTest : ApiAbstract<ProductApi>() {

    private lateinit var apiService: ProductApi

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var coroutineRule = MainCoroutinesRule()

    @Before
    fun setUp() {
        apiService = createService(ProductApi::class.java)
    }

    @Throws(IOException::class)
    @Test
    fun `test getProducts returns list of products`() = runBlocking {
        // Given
        enqueueResponse("/products.json")

        // When
        val response = apiService.getProducts()
        mockWebServer.takeRequest()

        // Then
        assertThat(response.size, `is`(2))
        assertThat(response[0].title, `is`("Fjallraven - Foldsack No. 1 Backpack"))
        assertThat(response[0].price, `is`(109.95))
        assertThat(response[0].category, `is`("men's clothing"))
        assertThat(response[0].rating.rate, `is`(3.9))
        assertThat(response[0].rating.count, `is`(120))

        assertThat(response[1].title, `is`("Mens Casual Premium Slim Fit T-Shirts"))
        assertThat(response[1].price, `is`(22.3))
        assertThat(response[1].rating.rate, `is`(4.1))
        assertThat(response[1].rating.count, `is`(259))
    }

    @Test
    fun `test getProducts request path is correct`() = runBlocking {
        // Given
        enqueueResponse("/products.json")

        // When
        apiService.getProducts()

        // Then
        assertRequestPath("/products")
    }
}