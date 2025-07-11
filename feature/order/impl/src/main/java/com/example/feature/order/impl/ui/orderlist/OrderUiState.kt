package com.example.feature.order.impl.ui.orderlist

import com.example.feature.order.api.domain.model.Order

data class OrderUiState(
    val order: Order? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
