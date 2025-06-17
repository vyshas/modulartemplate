package com.example.feature.home.impl.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.DomainResult
import com.example.feature.home.api.domain.model.HomeItem
import com.example.feature.home.api.domain.usecase.GetHomeItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeItemsUseCase: GetHomeItemsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchHomeItems()
    }

    private fun fetchHomeItems() {
        viewModelScope.launch {
            when (val result = getHomeItemsUseCase.getHomeItems()) {
                is DomainResult.Success -> _uiState.value = HomeUiState.Success(result.data)
                is DomainResult.Error -> _uiState.value = HomeUiState.Error(result.message)
                DomainResult.NetworkError -> _uiState.value = HomeUiState.Error("Network error")
            }
        }
    }
}

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val items: List<HomeItem>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}