package com.example.chillitest.data.repository

import com.example.chillitest.data.service.ApiService
import com.example.chillitest.data.states.ResultTypes
import com.example.chillitest.domain.models.DataResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GiphyRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun startSearch(query: String, limit: Int = 25, offset: Int = 0): ResultTypes<DataResponse> {
        return try {
            val response = apiService.searchGifs(query, limit, offset)
            when {
                response.isSuccessful -> ResultTypes.Success(response.body())
                else -> ResultTypes.HttpError(HttpException(response))
            }
        } catch (e: Throwable) {
            when (e) {
                is IOException -> ResultTypes.IOError(e)
                is HttpException -> ResultTypes.HttpError(e)
                else -> ResultTypes.Error(e.message)
            }
        }
    }
}