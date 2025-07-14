package com.example.testutils

import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Test

class TestDispatcherProviderTest {

    @Test
    fun `all dispatchers return the same test dispatcher`() {
        val testDispatcher = StandardTestDispatcher()
        val provider = TestDispatcherProvider(testDispatcher)

        assertEquals(testDispatcher, provider.main())
        assertEquals(testDispatcher, provider.io())
        assertEquals(testDispatcher, provider.default())
        assertEquals(testDispatcher, provider.mainImmediate())
        assertEquals(testDispatcher, provider.unconfined())
    }
}
