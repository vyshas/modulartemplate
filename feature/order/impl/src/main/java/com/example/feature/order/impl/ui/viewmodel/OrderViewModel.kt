package com.example.feature.order.impl.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.order.api.domain.usecase.GetOrderDetailsUseCase
import kotlinx.coroutines.launch

class OrderViewModel(
    private val useCase: GetOrderDetailsUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            useCase.getOrder()
        }
    }
}
