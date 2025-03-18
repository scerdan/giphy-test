package com.example.chillitest.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chillitest.presentation.view.DetailScreen
import com.example.chillitest.presentation.view.HomeScreen
import com.example.chillitest.presentation.viewmodel.GiphyViewModel

@Composable
fun NavigationGraph(
    viewModel: GiphyViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Screens.HOME.route) {
        composable(route = Screens.HOME.route) {
            HomeScreen(navController, viewModel)
        }

        composable(route = Screens.DETAIL.route) {
            DetailScreen(navController, viewModel)
        }
    }
}