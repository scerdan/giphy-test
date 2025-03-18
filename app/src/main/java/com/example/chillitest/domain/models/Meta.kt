package com.example.chillitest.domain.models


import androidx.annotation.Keep

@Keep
data class Meta(
    val msg: String,
    val response_id: String,
    val status: Int
)