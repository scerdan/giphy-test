package com.example.chillitest.data.service

import com.example.chillitest.domain.models.DataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("gifs/search")
    suspend fun searchGifs(
        @Query("q") query: String,
        @Query("limit") limit: Int = 25,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g",
        @Query("lang") lang: String = "en"
    ): Response<DataResponse>
}