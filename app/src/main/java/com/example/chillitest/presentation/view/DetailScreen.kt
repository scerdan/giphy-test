package com.example.chillitest.presentation.view

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.chillitest.R
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


    Scaffold(
        topBar = {
            Text(
                text = if (dataItems.data?.title?.isNullOrEmpty() == true) "Title Not Available" else dataItems.data?.title.toString(),
                fontFamily = FontFamily(Font(R.font.walsheimregular)),
                fontWeight = FontWeight(500),
                fontSize = 18.sp
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxHeight(9 / 10f)
                    .fillMaxWidth(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                AsyncImage(
                    model = dataItems.data?.images?.original?.url,
                    contentDescription = null,
                    imageLoader = gifImageLoader,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                )

                Text(
                    text = if (dataItems.data?.alt_text?.isNullOrEmpty() == true) "Not Description" else dataItems.data?.alt_text.toString(),
                    fontFamily = FontFamily(Font(R.font.walsheimregular)),
                    fontWeight = FontWeight(150),
                    fontSize = 12.sp
                )
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = if (dataItems.data?.import_datetime?.isNullOrEmpty() == true) "Datatime Not Available" else dataItems.data?.import_datetime.toString(),
                    fontFamily = FontFamily(Font(R.font.walsheimregular)),
                    fontWeight = FontWeight(500),
                    fontSize = 12.sp
                )
            }
        },
        modifier = Modifier
            .padding(16.dp, 32.dp),
    )
}
