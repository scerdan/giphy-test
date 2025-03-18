package com.example.chillitest.presentation.view

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.chillitest.data.states.ResultTypes
import com.example.chillitest.domain.models.Data
import com.example.chillitest.presentation.viewmodel.GiphyViewModel

@Composable
fun DetailScreen(navController: NavHostController, viewModel: GiphyViewModel) {
    val dataItems by viewModel.selectedItem.collectAsState()

    Column {
        when (dataItems) {
            is ResultTypes.Success -> {
                ShowDetails(dataItems as ResultTypes.Success<Data>)
            }

            else -> {

            }
        }
    }

}

@Composable
fun ShowDetails(dataItems: ResultTypes.Success<Data>) {
    val context = LocalContext.current
    val gifImageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()



        Column(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(vertical = 6.dp)
        ) {
            AsyncImage(
                model = dataItems.data?.images?.original?.url,
                contentDescription = null,
                imageLoader = gifImageLoader,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            )

//            Text(text = dataItems.data?.title ?: "Title not Found")
        }
}
