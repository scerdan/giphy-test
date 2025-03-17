package com.example.chillitest.domain.models

import com.google.gson.annotations.SerializedName

data class ImageDetails(
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int
)