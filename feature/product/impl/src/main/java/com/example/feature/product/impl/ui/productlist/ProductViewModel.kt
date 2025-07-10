package com.example.feature.product.impl.ui.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.coroutines.CoroutineDispatchers
import com.example.core.domain.DomainResult
import com.example.feature.product.api.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
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
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private val _uiEffect = MutableSharedFlow<ProductUiEffect>()
    val uiEffect: SharedFlow<ProductUiEffect> = _uiEffect.asSharedFlow()

    private val refreshTrigger = MutableSharedFlow<Unit>()

    // Create a scope with the injected dispatcher for better control
    private val vmScope = CoroutineScope(SupervisorJob() + dispatchers.main)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ProductUiState> = refreshTrigger
        .onStart { emit(Unit) } // Auto-trigger on start
        .flatMapLatest {
            flow {
                emit(ProductUiState.Loading)
                when (val result = getProductsUseCase.getProducts()) {
                    is DomainResult.Success -> emit(ProductUiState.Success(result.data))
                    is DomainResult.Error -> emit(ProductUiState.Error(result.message))
                    DomainResult.NetworkError -> emit(ProductUiState.Error("Network error"))
                }
            }.flowOn(dispatchers.io) // Use injected IO dispatcher for data operations
        }.stateIn(
            scope = vmScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProductUiState.Loading
        )

    fun onIntent(intent: ProductUiIntent) {
        when (intent) {
            ProductUiIntent.FetchProducts -> refresh()
            is ProductUiIntent.ProductClicked -> onProductClicked(intent.productId)
        }
    }

    private fun onProductClicked(productId: Int) {
        vmScope.launch(dispatchers.main) {
            _uiEffect.emit(ProductUiEffect.NavigateToProductDetail(productId))
        }
    }

    private fun refresh() {
        vmScope.launch(dispatchers.main) {
            refreshTrigger.emit(Unit)
        }
    }
}