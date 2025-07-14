package com.example.feature.templatefeature.impl.remote

import com.example.testutils.ApiAbstract
import com.example.testutils.MainCoroutinesRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TemplateFeatureApiTest : ApiAbstract<TemplateFeatureApi>() {

    private lateinit var apiService: TemplateFeatureApi

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var coroutineRule = MainCoroutinesRule()

    @Before
    fun setUp() {
        apiService = createService(TemplateFeatureApi::class.java)
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
