package com.example.chillitest

import com.example.chillitest.data.service.ApiService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Test class for verifying the functionality of the [NetworkModule].
 * This class tests the creation and behavior of [OkHttpClient], [Retrofit], and [ApiService].
 */
class NetworkModuleTest {

    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService
    private lateinit var mockWebServer: MockWebServer

    private val apiKey = "Bs3FrM2bXJrvWdSwT2gPMjPJ5XkNJyEE"

    /**
     * Sets up the test environment before each test case.
     * Initializes [MockWebServer], configures [OkHttpClient] with logging and API key interceptors,
     * and sets up [Retrofit] with the base URL from [MockWebServer].
     */
    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val loggingInterceptor = Interceptor { chain ->
            val request = chain.request()
            println("Request: ${request.method} ${request.url}")
            println("Headers: ${request.headers}")

            val response = chain.proceed(request)
            val responseBody = response.body
            val responseBodyString = responseBody?.string() ?: ""

            val newResponse = response.newBuilder()
                .body(responseBodyString.toResponseBody(responseBody?.contentType()))
                .build()

            println("Response: ${newResponse.code}")
            println("Response Body: $responseBodyString")

            return@Interceptor newResponse
        }

        val apiKeyInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val newUrl = originalRequest.url.newBuilder()
                .addQueryParameter("api_key", apiKey)
                .build()
            val newRequest = originalRequest.newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
        }

        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    /**
     * Tears down the test environment after each test case.
     * Shuts down the [MockWebServer].
     */
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    /**
     * Tests that the [OkHttpClient] instance is created and is not null.
     */
    @Test
    fun testProvideOkHttpClient() {
        assertThat(okHttpClient).isNotNull()
    }

    /**
     * Tests that the [Retrofit] instance is created and is not null.
     * Verifies that the base URL matches the one provided by [MockWebServer].
     */
    @Test
    fun testProvideRetrofit() {
        assertThat(retrofit).isNotNull()
        assertThat(retrofit.baseUrl().toString()).isEqualTo(mockWebServer.url("/").toString())
    }

    /**
     * Tests that the API key is valid and the API responds with a 200 status code.
     */
    @Test
    fun testApiKeyValid() = runBlocking {
        val mockResponse = MockResponse()
            .setBody("{\"data\": []}")
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        val response = apiService.searchGifs("test", 10, 0)

        assertThat(response.isSuccessful).isTrue()
        assertThat(response.code()).isEqualTo(200)
    }

    /**
     * Tests that the API key is invalid or the rate limit is exceeded, and the API responds with a 429 status code.
     */
    @Test
    fun testApiKeyInvalidOrRateLimitExceeded() = runBlocking {
        val mockResponse = MockResponse()
            .setBody("{\"message\": \"Too many requests\"}")
            .setResponseCode(429)
        mockWebServer.enqueue(mockResponse)

        val response = apiService.searchGifs("test", 10, 0)

        assertThat(response.isSuccessful).isFalse()
        assertThat(response.code()).isEqualTo(429)
    }
}