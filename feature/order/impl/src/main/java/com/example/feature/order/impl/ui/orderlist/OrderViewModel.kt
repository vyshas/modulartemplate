package com.example.feature.order.impl.ui.orderlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.coroutines.DispatcherProvider
import com.example.core.domain.DomainResult
import com.example.feature.order.api.domain.usecase.GetOrderDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getOrderDetailsUseCase: GetOrderDetailsUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiEffect = MutableSharedFlow<OrderUiEffect>()
    val uiEffect: SharedFlow<OrderUiEffect> = _uiEffect.asSharedFlow()

    private val refreshTrigger = MutableSharedFlow<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<OrderUiState> = refreshTrigger
        .onStart { emit(Unit) } // Auto-trigger on start
        .flatMapLatest {
            flow {
                emit(OrderUiState(isLoading = true))
                try {
                    val order = getOrderDetailsUseCase.getOrder()
                    emit(OrderUiState(isLoading = false, order = order))
                } catch (e: Exception) {
                    emit(OrderUiState(isLoading = false, error = e.message ?: "Unknown error"))
                }
            }.flowOn(dispatcherProvider.io())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = OrderUiState(isLoading = true)
        )

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
                refresh()
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            refreshTrigger.emit(Unit)
        }
    }
}