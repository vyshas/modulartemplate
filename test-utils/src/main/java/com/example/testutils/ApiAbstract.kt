package com.example.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.buffer
import okio.source
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.nio.charset.StandardCharsets

@RunWith(JUnit4::class)
abstract class ApiAbstract<T> {
    @Rule
    @JvmField
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var mockWebServer: MockWebServer

    @Throws(IOException::class)
    @Before
    fun mockServer() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @Throws(IOException::class)
    @After
    fun stopServer() {
        mockWebServer.shutdown()
    }

    @Throws(IOException::class)
    fun enqueueResponse(fileName: String) {
        enqueueResponse(fileName, emptyMap())
    }

    @Throws(IOException::class)
    fun enqueueResponse(fileName: String, headers: Map<String, String>) {
        val inputStream = javaClass.classLoader?.getResourceAsStream("api-response/$fileName")
            ?: throw IOException("File not found: api-response/$fileName")
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(mockResponse.setBody(source.readString(StandardCharsets.UTF_8)))
    }

    fun createService(clazz: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build(),
                ),
            )
            .build()
            .create(clazz)
    }

    fun assertRequestPath(path: String) {
        val request: RecordedRequest = mockWebServer.takeRequest()
        MatcherAssert.assertThat(request.path, CoreMatchers.`is`(path))
    }
}
