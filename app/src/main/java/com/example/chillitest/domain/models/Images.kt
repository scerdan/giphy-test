package com.example.chillitest.domain.models

import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("original")
    val original: ImageDetails
)