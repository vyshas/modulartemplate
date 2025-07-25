package com.example.feature.home.impl.ui.homelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.coroutines.DispatcherProvider
import com.example.core.domain.DomainResult
import com.example.feature.home.api.domain.model.HomeItem
import com.example.feature.home.api.domain.usecase.GetHomeItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeItemsUseCase: GetHomeItemsUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val refreshTrigger = MutableSharedFlow<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HomeUiState> = refreshTrigger
        .onStart { emit(Unit) } // Auto-trigger on start
        .flatMapLatest {
            flow {
                emit(HomeUiState.Loading)
                when (val result = getHomeItemsUseCase.getHomeItems()) {
                    is DomainResult.Success -> emit(HomeUiState.Success(result.data))
                    is DomainResult.Error -> emit(HomeUiState.Error(result.message))
                    DomainResult.NetworkError -> emit(HomeUiState.Error("Network error"))
                }
            }.flowOn(dispatcherProvider.io())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState.Loading,
        )

    fun refresh() {
        viewModelScope.launch {
            refreshTrigger.emit(Unit)
        }
    }
}

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val items: List<HomeItem>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
