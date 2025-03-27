package com.example.chillitest.presentation.navigation

import androidx.navigation.NavController

sealed class Screens(val route: String) {
    object HOME : Screens("HomeScreen")
    object DETAIL : Screens("DetailScreen")

}

/**
 * Navigates to the specified destination using the provided NavController.
 *
 * @param direction The route or destination to navigate to.
 * @param navController The NavController instance for managing navigation.
 * @param offset A boolean indicating whether to pop the back stack before navigating.
 */
fun goTo(direction: String, navController: NavController, clearBackStack: Boolean) {
    if (clearBackStack) {
        navController.navigate(direction) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    } else {
        navController.navigate(direction)
    }
}