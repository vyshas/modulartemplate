package com.example.feature.order.impl.ui.orderlist

sealed interface OrderUiIntent {
    object FetchOrder : OrderUiIntent
    object OnViewHomeDetailClicked : OrderUiIntent
}
