package com.example.chillitest.domain.models


import androidx.annotation.Keep

@Keep
data class Analytics(
    val onclick: Onclick,
    val onload: Onload
)