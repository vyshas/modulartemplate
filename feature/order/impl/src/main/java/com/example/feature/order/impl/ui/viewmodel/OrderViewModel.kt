package com.example.feature.order.impl.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.order.api.domain.usecase.GetOrderDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getOrderDetailsUseCase: GetOrderDetailsUseCase
) : ViewModel() {

/*    private val _uiState = MutableStateFlow<OrderUiState>(OrderUiState.Loading)
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    init {
        fetchHomeItems()
    }

    private fun fetchHomeItems() {
        viewModelScope.launch {
            when (val result = getOrderDetailsUseCase.getOrder()) {

            }
        }
    }*/
}

/*sealed class OrderUiState {
    data object Loading : OrderUiState()
    data class Success(val items: List<OrderItem>) : OrderUiState()
    data class Error(val message: String) : OrderUiState()
}*/
