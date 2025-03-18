package com.example.chillitest.presentation.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.chillitest.R
import com.example.chillitest.data.repository.PaginationHandler
import com.example.chillitest.data.states.ResultTypes
import com.example.chillitest.domain.models.Data
import com.example.chillitest.domain.models.DataResponse
import com.example.chillitest.presentation.viewmodel.GiphyViewModel

@Composable
fun MainScreen(viewModel: GiphyViewModel = hiltViewModel()) {
    var searchQuery by remember { mutableStateOf("") }

    val gifState by viewModel.gifState.collectAsState()
    val emptyComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty))
    val networkErrorComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.internet_error))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // SearchBar
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(50.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_icon),
                contentDescription = "logo",
                modifier = Modifier.size(20.dp)
            )
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search GIFs") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.searchGifs(searchQuery, isNewSearch = true)
                    }
                )
            )
        }

        when (gifState) {
            is ResultTypes.Error -> TODO()
            is ResultTypes.HttpError -> {
                LottieAnimation(
                    composition = networkErrorComposition,
                    iterations = Int.MAX_VALUE,
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
            is ResultTypes.IOError -> TODO()
            ResultTypes.Loading -> {
                //TODO Crear Pantalla general indicando que se debe buscar en la barra
                LottieAnimation(
                    composition = emptyComposition,
                    iterations = Int.MAX_VALUE,
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            is ResultTypes.Success -> {
                val data = (gifState as ResultTypes.Success<DataResponse>).data?.data
                ShowAll(data, viewModel, searchQuery)
            }
        }
    }
}

@Composable
fun ShowAll(data: List<Data>?, viewModel: GiphyViewModel, searchQuery: String) {
    val context = LocalContext.current
    val loadComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.load))
    val isLastItemVisible = remember { mutableStateOf(false) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize()
    ) {
        data?.let {
            items(it.size) { count ->
                SubcomposeAsyncImage(
                    model = it[count].images.original.url,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(1.dp)
                        .aspectRatio(1f),
                    loading = {
                        LottieAnimation(
                            composition = loadComposition,
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    },
                    alignment = Alignment.Center
                )

                if (count == it.size - 1) {
                    Toast.makeText(context, "Next Page", Toast.LENGTH_LONG).show()
                    isLastItemVisible.value = true
                }
            }
        }
    }

    PaginationHandler(
        isLastItemVisible = isLastItemVisible.value,
        onLoadMore = {
            viewModel.searchGifs(searchQuery, isNewSearch = false)
            isLastItemVisible.value = false
        }
    )
}
