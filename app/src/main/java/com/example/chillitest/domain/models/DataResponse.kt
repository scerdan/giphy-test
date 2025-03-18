package com.example.chillitest.domain.models


import androidx.annotation.Keep

@Keep
data class DataResponse(
    val `data`: List<Data>,
    val meta: Meta,
    val pagination: Pagination
)