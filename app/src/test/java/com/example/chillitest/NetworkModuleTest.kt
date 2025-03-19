package com.example.chillitest

import com.example.chillitest.data.service.ApiService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModuleTest {

    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService
    private lateinit var mockWebServer: MockWebServer

    private val apiKey = "Bs3FrM2bXJrvWdSwT2gPMjPJ5XkNJyEE"  // Tu API key real

    @Before
    fun setUp() {
        // Inicializa el MockWebServer
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val loggingInterceptor = Interceptor { chain ->
            val request = chain.request()

            // Loguear la solicitud
            println("Request: ${request.method} ${request.url}")
            println("Headers: ${request.headers}")

            val response = chain.proceed(request)

            // Obtener el cuerpo de la respuesta y almacenarlo como una cadena
            val responseBody = response.body
            val responseBodyString = responseBody?.string() ?: "" // Lee el cuerpo y lo convierte a String

            // Crear un nuevo Response con el cuerpo de la respuesta (sin cerrarlo)
            val newResponse = response.newBuilder()
                .body(okhttp3.ResponseBody.create(responseBody?.contentType(), responseBodyString))
                .build()

            // Loguear la respuesta
            println("Response: ${newResponse.code}")
            println("Response Body: $responseBodyString")

            return@Interceptor newResponse // Retorna la respuesta modificada
        }


        // Configura OkHttpClient con el interceptor de log y el interceptor de API Key
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)  // Agrega el interceptor de logging
            .build()

        // Configura Retrofit con la URL base de la API y el OkHttpClient configurado
        retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))  // Usa la URL de MockWebServer
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Crea el servicio de la API
        apiService = retrofit.create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        // Detiene MockWebServer
        mockWebServer.shutdown()
    }

    @Test
    fun testProvideOkHttpClient() {
        assertThat(okHttpClient).isNotNull()
    }

    @Test
    fun testProvideRetrofit() {
        assertThat(retrofit).isNotNull()
        assertThat(retrofit.baseUrl().toString()).isEqualTo(mockWebServer.url("/").toString())
    }

    @Test
    fun testProvideGiphyApiWithApiKey() = runBlocking {
        // Simula una respuesta exitosa (por ejemplo, una lista vacía de datos)
        val mockResponse = MockResponse()
            .setBody("{\"data\": []}")  // Respuesta vacía, válida pero sin resultados
            .setResponseCode(200)  // Respuesta 200 OK
        mockWebServer.enqueue(mockResponse)

        // Realiza la solicitud simulada usando el método de búsqueda (con "test" como ejemplo)
        val response = apiService.searchGifs("test", 10, 0)

        // Verifica que la respuesta no sea nula
        assertThat(response).isNotNull()

        // Verifica que el cuerpo de la respuesta no sea nulo
        val body = response.body()
        assertThat(body).isNotNull()

        // Verifica que la lista de datos no sea nula y esté vacía
        assertThat(body?.data).isNotNull()
        assertThat(body?.data).isEmpty()  // La lista está vacía pero es válida
    }
}
