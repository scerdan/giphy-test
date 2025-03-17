package com.example.chillitest.domain.models

import com.google.gson.annotations.SerializedName

data class DataResponse(
    @SerializedName("data")
    val data: List<Data>
)