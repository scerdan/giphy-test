package com.example.chillitest.presentation.view

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.chillitest.data.states.ResultTypes
import com.example.chillitest.presentation.viewmodel.GiphyViewModel

@Composable
fun DetailScreen(navController: NavHostController, viewModel: GiphyViewModel) {
    val dataItems by viewModel.selectedItem.collectAsState()
    val context = LocalContext.current

    val gifImageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory()) // Usar ImageDecoder en Android 9.0+ (API 28+)
            } else {
                add(GifDecoder.Factory()) // Usar GifDecoder en versiones anteriores
            }
        }
        .build()

    Column {
        when (dataItems) {
            is ResultTypes.Success -> {
                AsyncImage(
                    model = (dataItems as ResultTypes.Success).data?.images?.original?.url,
                    contentDescription = null,
                    imageLoader = gifImageLoader,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                )
            }

            else -> {

            }
        }
    }

}