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

                when(errorMessage.exception.code()) {
                    429 -> {
                        Text("Your API Key is making too many requests.",
                            fontFamily = FontFamily(Font(R.font.walsheimregular)),
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
            }
            else -> {}
        }
    }
}