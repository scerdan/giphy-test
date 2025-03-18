package com.example.chillitest.presentation.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.chillitest.R
import com.example.chillitest.data.repository.PaginationHandler
import com.example.chillitest.data.states.ResultTypes
import com.example.chillitest.presentation.viewmodel.GiphyViewModel

@Composable
fun MainScreen(viewModel: GiphyViewModel = hiltViewModel()) {
    var searchQuery by remember { mutableStateOf("") }

    val context = LocalContext.current
    val gifState by viewModel.gifState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Estado para detectar si el último elemento es visible
    val isLastItemVisible = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // SearchBar
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search GIFs") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // Realizar una nueva búsqueda
                    viewModel.searchGifs(searchQuery, isNewSearch = true)
                }
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (gifState) {
                is ResultTypes.Success -> {
                    val data =
                        (gifState as ResultTypes.Success).data?.data // Acceder a la lista de `data`

                    if (data != null) {
                        items(data.size) { count ->
                            SubcomposeAsyncImage(
                                model = data[count].images.original.url,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(1.dp)
                                    .aspectRatio(1f),
                                loading = {
                                    Image(
                                        painter = painterResource(id = R.drawable.logo_icon),
                                        contentDescription = "Loading...",
                                        modifier = Modifier.size(50.dp)
                                    )
                                }
                            )

                            if (count == data.size - 1) {
                                Toast.makeText(
                                    context,
                                    "next Page",
                                    Toast.LENGTH_LONG
                                ).show()
                                isLastItemVisible.value = true
                            }
                        }
                    }
                }

                is ResultTypes.Error -> {
                    item {
                        Text(
                            text = "Error: ${(gifState as ResultTypes.Error)}",
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                is ResultTypes.HttpError -> {
                    item {
                        Text(
                            text = "HTTP Error: ${(gifState as ResultTypes.HttpError).exception.code()}",
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                is ResultTypes.IOError -> {
                    item {
                        Text(
                            text = "Network Error: ${(gifState as ResultTypes.IOError).exception.message}",
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                is ResultTypes.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally)
                        )
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
}
