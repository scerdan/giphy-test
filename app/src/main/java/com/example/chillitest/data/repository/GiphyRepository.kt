package com.example.chillitest.data.repository

import com.example.chillitest.data.service.ApiService
import javax.inject.Inject

class GiphyRepository @Inject constructor(private val client: ApiService) {

    suspend fun getGiffs(query: String) {
        client.randomGifs(apiKey = BuildConfig, q = query)
    }
}