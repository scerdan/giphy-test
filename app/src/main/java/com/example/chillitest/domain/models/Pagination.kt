package com.example.chillitest.domain.models


import androidx.annotation.Keep

@Keep
data class Pagination(
    val count: Int,
    val offset: Int,
    val total_count: Int
)