package com.example.feature.home.impl.ui.homedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.DomainResult
import com.example.feature.home.api.domain.model.HomeItem
import com.example.feature.home.api.domain.usecase.GetHomeItemByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getHomeItemByIdUseCase: GetHomeItemByIdUseCase
) : ViewModel() {

    private val itemId = savedStateHandle.get<Int>("itemId") ?: -1

    private val _uiState = MutableStateFlow<HomeDetailUiState>(HomeDetailUiState.Loading)
    val uiState: StateFlow<HomeDetailUiState> = _uiState

    init {
        viewModelScope.launch {
            when (val result = getHomeItemByIdUseCase(itemId)) {
                is DomainResult.Success -> _uiState.value = HomeDetailUiState.Success(result.data)
                is DomainResult.Error -> _uiState.value = HomeDetailUiState.Error(result.message)
                DomainResult.NetworkError -> _uiState.value =
                    HomeDetailUiState.Error("Network error")
            }
        }
    }
}

sealed interface HomeDetailUiState {
    data object Loading : HomeDetailUiState
    data class Success(val item: HomeItem) : HomeDetailUiState
    data class Error(val message: String) : HomeDetailUiState
}

