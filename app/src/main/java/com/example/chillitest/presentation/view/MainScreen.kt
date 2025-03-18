package com.example.chillitest.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.xr.compose.testing.toDp
import coil.compose.AsyncImage
import com.example.chillitest.data.states.ResultTypes
import com.example.chillitest.presentation.viewmodel.GiphyViewModel

@Composable
fun GiphyScreen(viewModel: GiphyViewModel = hiltViewModel()) {
    var searchQuery by remember { mutableStateOf("") }

    val gifs by viewModel.gifState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
                    viewModel.searchGifs(searchQuery)
                }
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize()
        ) {
            when (gifs) {
                is ResultTypes.Success -> {
                    val data = (gifs as ResultTypes.Success).data?.data

                    if (data != null) {
                        items(data.size) { count ->
                            AsyncImage(
                                model = data[count].images.original.url,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(1.dp)
                                    .aspectRatio(1f)
                                    .width(data[count].images.original.width.dp)
                                    .height(data[count].images.original.height.dp)
                            )
                        }
                    }
                }

                is ResultTypes.Error -> {}
                is ResultTypes.HttpError ->{}
                is ResultTypes.IOError -> {}
                is ResultTypes.Loading -> {
                }
            }
        }
    }
}