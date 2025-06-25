package com.example.feature.order.impl.ui.orderlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.order.api.domain.usecase.GetOrderDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getOrderDetailsUseCase: GetOrderDetailsUseCase
) : ViewModel() {

    private val _uiEffect = MutableSharedFlow<OrderUiEffect>()
    val uiEffect: SharedFlow<OrderUiEffect> = _uiEffect.asSharedFlow()

    fun onIntent(intent: OrderUiIntent) {
        when (intent) {
            is OrderUiIntent.OnViewHomeDetailClicked -> {
                viewModelScope.launch {
                    _uiEffect.emit(OrderUiEffect.NavigateToHomeDetail(intent.homeId))
                }
            }
        }
    }
}