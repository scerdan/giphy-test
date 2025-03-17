package com.example.chillitest.data.service

import com.example.chillitest.domain.models.DataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("gifs/search")
    suspend fun randomGifs(
        @Query("api_key") apiKey: String,
        @Query("q") q: String
    ): Response<DataResponse?>
}