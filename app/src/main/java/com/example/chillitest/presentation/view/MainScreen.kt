package com.example.chillitest.presentation.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.chillitest.R
import com.example.chillitest.data.repository.PaginationHandler
import com.example.chillitest.data.states.ResultTypes
import com.example.chillitest.domain.models.Data
import com.example.chillitest.domain.models.DataResponse
import com.example.chillitest.presentation.viewmodel.GiphyViewModel
import kotlinx.coroutines.delay

@Composable
fun MainScreen(viewModel: GiphyViewModel = hiltViewModel()) {
    var searchQuery by remember { mutableStateOf("") }

    val gifState by viewModel.gifState.collectAsState()
    val emptyComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty))
    val networkErrorComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.internet_error))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 32.dp)
    ) {
        Text(
            "Welcome!",
            fontFamily = FontFamily(Font(R.font.walsheimregular)),
            fontWeight = FontWeight(500),
            fontSize = 28.sp
        )

        CustomSearchBar(
            searchQuery = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = {
                viewModel.searchGifs(searchQuery, isNewSearch = true)
            }
        )

        when (gifState) {
            is ResultTypes.Error -> {
                val errorMessage = (gifState as ResultTypes.Error)
                Text(text = "Error: $errorMessage", color = androidx.compose.ui.graphics.Color.Red)
            }

            is ResultTypes.HttpError -> {
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

            ResultTypes.Loading -> {
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
                if (data.isNullOrEmpty()) {
                    LottieAnimation(
                        composition = emptyComposition,
                        iterations = Int.MAX_VALUE,
                        modifier = Modifier
                            .fillMaxSize(1f)
                            .clip(RoundedCornerShape(12.dp))
                    )
                } else {
                    ShowAll(data = data, viewModel = viewModel, searchQuery = searchQuery)
                }
            }
        }
    }
}

@Composable
fun CustomSearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            delay(250)
            onSearch()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_icon),
            contentDescription = "logo",
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                onQueryChange(query)
            },
            modifier = Modifier
                .weight(1f)
                .height(60.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && isFocused) {
                        keyboardController?.hide()
                    }
                    isFocused = focusState.isFocused
                },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onSearch()
                }
            ),
            placeholder = {
                Text(text = "Search gifs")
            }
        )
    }
}

@Composable
fun ShowAll(data: List<Data>, viewModel: GiphyViewModel, searchQuery: String) {
    val context = LocalContext.current
    val loadComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.load))
    val isLastItemVisible = remember { mutableStateOf(false) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize()
            .padding(vertical = 6.dp)
    ) {
        items(data.size) { count ->
            SubcomposeAsyncImage(
                model = data[count].images.original.url,
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
                error = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Error",
                        modifier = Modifier.size(30.dp)
                    )
                },
                alignment = Alignment.Center
            )

            if (count == data.size - 1) {
                Toast.makeText(context, "Next Page", Toast.LENGTH_LONG).show()
                isLastItemVisible.value = true
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