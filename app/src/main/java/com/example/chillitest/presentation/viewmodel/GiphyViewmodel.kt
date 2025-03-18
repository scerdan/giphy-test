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

    // Estado para los GIFs
    private val _gifState = MutableStateFlow<ResultTypes<DataResponse>>(ResultTypes.Loading)
    val gifState: StateFlow<ResultTypes<DataResponse>> get() = _gifState

    // Estado para el mensaje de error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    // Estado para el indicador de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private var currentQuery = ""
    private var offset = 0
    private val limit = 25 // Número de GIFs por página

    // Función para buscar GIFs
    fun searchGifs(query: String, isNewSearch: Boolean = true) {
        if (_isLoading.value) return // Evitar múltiples llamadas

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

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
                        _errorMessage.value = result.exception
                        _gifState.value = ResultTypes.Error(result.exception)
                    }

                    is ResultTypes.HttpError -> {
                        _errorMessage.value = "HTTP Error: ${result.exception.message}"
                        _gifState.value = ResultTypes.HttpError(result.exception)
                    }

                    is ResultTypes.IOError -> {
                        _errorMessage.value = "Network Error: ${result.exception.message}"
                        _gifState.value = ResultTypes.IOError(result.exception)
                    }

                    else -> {
                        _errorMessage.value = "Unknown error"
                        _gifState.value = ResultTypes.Error("Unknown error")
                    }
                }

                offset += limit
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error desconocido"
                _gifState.value = ResultTypes.Error(e.message ?: "Error desconocido")
            } finally {
                _isLoading.value = false
            }
        }
    }
}