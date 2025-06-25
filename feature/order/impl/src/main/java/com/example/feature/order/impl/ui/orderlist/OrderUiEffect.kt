package com.example.feature.order.impl.ui.orderlist

sealed interface OrderUiEffect {
    data class NavigateToHomeDetail(
        val homeId: Int
    ) : OrderUiEffect
}
