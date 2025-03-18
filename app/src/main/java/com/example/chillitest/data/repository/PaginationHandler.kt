package com.example.chillitest.data.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun PaginationHandler(
    isLastItemVisible: Boolean,
    onLoadMore: () -> Unit
) {
    LaunchedEffect(isLastItemVisible) {
        if (isLastItemVisible) {
            onLoadMore()
        }
    }
}