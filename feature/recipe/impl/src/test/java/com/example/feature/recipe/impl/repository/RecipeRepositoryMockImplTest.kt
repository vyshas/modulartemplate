package com.example.feature.recipe.impl.repository

import android.content.Context
import org.junit.Before
import org.junit.After
import java.io.InputStream

class RecipeRepositoryMockImplTest {

    @Before
    fun setUp() {
        // Setup for mock repository tests
    }

    @After
    fun tearDown() {
        // Cleanup for mock repository tests
    }

    // Utility function for reading test json, can be used by custom tests
    fun readJsonFromAssets(context: Context, fileName: String): String {
        return try {
            val inputStream: InputStream = context.assets.open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}