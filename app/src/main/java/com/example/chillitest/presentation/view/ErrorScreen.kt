package com.example.chillitest.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.chillitest.R
import com.example.chillitest.data.states.ResultTypes
import com.example.chillitest.domain.models.DataResponse

@Composable
fun ErrorScreen(errorMessage: ResultTypes<DataResponse>) {
//    val errorMessage = (errorMessage as ResultTypes.HttpError)
    val networkErrorComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.internet_error))
    val tokenComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))

    Column(
        modifier = Modifier
            .fillMaxSize(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when(errorMessage) {
            is ResultTypes.Error -> {
                LottieAnimation(
                    composition = tokenComposition,
                    iterations = Int.MAX_VALUE,
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
            is ResultTypes.HttpError -> {
                LottieAnimation(
                    composition = tokenComposition,
                    iterations = Int.MAX_VALUE,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .width(400.dp)
                        .height(250.dp)
                )

                when (errorMessage.exception.code()) {
                    400 -> {
                        Text(
                            "Your request was formatted incorrectly or missing a required parameter(s).",
                            fontFamily = FontFamily(Font(R.font.walsheimregular)),
                            fontWeight = FontWeight(300)
                        )
                    }
                    401 -> {
                        Text(
                            "Your request lacks valid authentication credentials for the target resource, which most likely indicates an issue with your API Key or the API Key is missing.",
                            fontFamily = FontFamily(Font(R.font.walsheimregular)),
                            fontWeight = FontWeight(300)
                        )
                    }
                    403 -> {
                        Text(
                            "You weren't authorized to make your request; most likely this indicates an issue with your API Key.",
                            fontFamily = FontFamily(Font(R.font.walsheimregular)),
                            fontWeight = FontWeight(300)
                        )
                    }
                    404 -> {
                        Text(
                            "The particular GIF or Sticker you are requesting was not found. This occurs, for example, if you request a GIF by using an id that does not exist.",
                            fontFamily = FontFamily(Font(R.font.walsheimregular)),
                            fontWeight = FontWeight(300)
                        )
                    }
                    414 -> {
                        Text(
                            "The length of the search query exceeds 50 characters.",
                            fontFamily = FontFamily(Font(R.font.walsheimregular)),
                            fontWeight = FontWeight(300)
                        )
                    }
                    429 -> {
                        Text(
                            "Your API Key is making too many requests.",
                            fontFamily = FontFamily(Font(R.font.walsheimregular)),
                            fontWeight = FontWeight(300)
                        )
                    }
                    else -> {
                        Text(
                            "An unexpected error occurred.",
                            fontFamily = FontFamily(Font(R.font.walsheimregular)),
                            fontWeight = FontWeight(300)
                        )
                    }
                }
            }

            is ResultTypes.IOError -> {
                LottieAnimation(
                    composition = networkErrorComposition,
                    iterations = Int.MAX_VALUE,
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .clip(RoundedCornerShape(12.dp))
                )

                Text(
                    "A network error occurred. Please check your internet connection and try again.",
                    fontFamily = FontFamily(Font(R.font.walsheimregular)),
                    fontWeight = FontWeight(300)
                )
            }
            else -> {}
        }
    }
}