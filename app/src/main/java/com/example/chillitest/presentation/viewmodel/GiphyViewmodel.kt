package com.example.chillitest.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chillitest.data.repository.GiphyRepository
import com.example.chillitest.data.states.ResultTypes
import com.example.chillitest.domain.models.Data
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

    private val _gifState = MutableStateFlow<ResultTypes<DataResponse>>(ResultTypes.Loading)
    val gifState: StateFlow<ResultTypes<DataResponse>> get() = _gifState

    private val _selectedItem = MutableStateFlow<ResultTypes<Data>>(ResultTypes.Loading)
    val selectedItem: StateFlow<ResultTypes<Data>> get() = _selectedItem


    private var currentQuery = ""
    private var offset = 0
    private val limit = 25

    fun searchGifs(query: String, isNewSearch: Boolean = true) {
        viewModelScope.launch {
            try {
                if (isNewSearch) {
                    offset = 0
                    currentQuery = query
                    _gifState.value = ResultTypes.Loading
                }

                val result = repository.startSearch(currentQuery, limit, offset)

                when (result) {
                    is ResultTypes.Success -> {
                        val responseData = result.data
                        if (responseData != null) {
                            if (isNewSearch) {
                                _gifState.value = ResultTypes.Success(responseData)
                            } else {
                                val currentData = (_gifState.value as? ResultTypes.Success)?.data
                                if (currentData != null) {
                                    val newDataList = currentData.data.toMutableList().apply {
                                        addAll(responseData.data)
                                    }
                                    val newDataResponse = currentData.copy(data = newDataList)
                                    _gifState.value = ResultTypes.Success(newDataResponse)
                                }
                            }
                        }
                    }

                    is ResultTypes.Error -> {
                        _gifState.value = ResultTypes.Error(result.exception)
                    }

                    is ResultTypes.HttpError -> {
                        _gifState.value = ResultTypes.HttpError(result.exception)
                    }

                    is ResultTypes.IOError -> {
                        _gifState.value = ResultTypes.IOError(result.exception)
                    }

                    else -> {
                        _gifState.value = ResultTypes.Error("Unknown error")
                    }
                }

                offset += limit
            } catch (e: Exception) {
                _gifState.value = ResultTypes.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun selectItem(item: Data) {
        _selectedItem.value = ResultTypes.Success(item)
    }

    fun clearSelectedItem() {
        _selectedItem.value = ResultTypes.Loading
    }
}