package com.example.chillitest.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chillitest.data.states.ResultTypes
import com.example.chillitest.presentation.viewmodel.GiphyViewModel
import com.example.chillitest.ui.theme.ChilliTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChilliTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen()
                }
            }
        }
    }
}
//
//@Composable
//fun GiphyScreen(viewModel: GiphyViewModel = hiltViewModel()) {
//    val gifs by viewModel.gifState.collectAsState()
//    viewModel.searchGifs("CATS")
//
//    when(gifs) {
//        is ResultTypes.Error -> TODO()
//        is ResultTypes.HttpError -> TODO()
//        is ResultTypes.IOError -> TODO()
//        is ResultTypes.Loading -> TODO()
//        is ResultTypes.Success -> TODO()
//    }
//}
