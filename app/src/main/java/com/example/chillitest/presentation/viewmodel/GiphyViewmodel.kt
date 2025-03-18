package com.example.chillitest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chillitest.data.repository.GiphyRepository
import com.example.chillitest.data.states.ResultTypes
import com.example.chillitest.domain.models.DataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GiphyViewModel @Inject constructor(
    private val repository: GiphyRepository
) : ViewModel() {

    private val _gifState = MutableStateFlow<ResultTypes<DataResponse>>(
        ResultTypes.Loading
    )
    val gifState: StateFlow<ResultTypes<DataResponse>> get() = _gifState

    fun searchGifs(query: String) {
        viewModelScope.launch {
            _gifState.value = repository.startSearch(query)
        }
    }
}