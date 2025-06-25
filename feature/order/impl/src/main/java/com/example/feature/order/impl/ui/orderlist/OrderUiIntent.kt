package com.example.feature.order.impl.ui.orderlist

sealed interface OrderUiIntent {
    data class OnViewHomeDetailClicked(val homeId: Int) : OrderUiIntent
}

