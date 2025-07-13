package com.example.feature.templatefeature.impl.remote

import com.example.feature.templatefeature.impl.data.ApiTemplateFeature
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

class TemplateFeatureApiTest : ApiAbstract<TemplateFeatureApi>() {

    private lateinit var apiService: TemplateFeatureApi

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var coroutineRule = MainCoroutinesRule()

    @Before
    fun setUp() {
        apiService = createService(TemplateFeatureApi::class.java)
    }

    @Throws(IOException::class)
    @Test
    fun `test getTemplateFeatures returns list of template features`() = runBlocking {
        // Given
        enqueueResponse("/templatefeatures.json")

        // When
        val response = apiService.getTemplateFeatures()
        mockWebServer.takeRequest()

        // Then
        //assertThat(response.size, `is`(2))
    }

    @Test
    fun `test getTemplateFeatures request path is correct`() = runBlocking {
        // Given
        enqueueResponse("/templatefeatures.json")

        // When
        apiService.getTemplateFeatures()

        // Then
        assertRequestPath("/templatefeatures")
    }
}