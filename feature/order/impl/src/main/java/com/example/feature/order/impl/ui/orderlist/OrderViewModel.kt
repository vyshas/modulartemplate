package com.example.feature.order.impl.ui.orderlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.order.api.domain.usecase.GetOrderDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getOrderDetailsUseCase: GetOrderDetailsUseCase
) : ViewModel() {

    val uiState: StateFlow<OrderUiState> = flow {
        emit(OrderUiState(isLoading = true))
        val order = getOrderDetailsUseCase.getOrder()
        emit(OrderUiState(isLoading = false, order = order))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = OrderUiState(isLoading = true)
    )
    private val _uiEffect = MutableSharedFlow<OrderUiEffect>()
    val uiEffect: SharedFlow<OrderUiEffect> = _uiEffect.asSharedFlow()

    fun onIntent(intent: OrderUiIntent) {
        when (intent) {
            is OrderUiIntent.OnViewHomeDetailClicked -> {
                viewModelScope.launch {
                    val order = uiState.value.order
                    if (order != null) {
                        _uiEffect.emit(OrderUiEffect.NavigateToHomeDetail(order.orderId))
                    }
                }
            }

            is OrderUiIntent.FetchOrder -> {
                // Could implement refresh logic here if needed
            }
        }
    }
}