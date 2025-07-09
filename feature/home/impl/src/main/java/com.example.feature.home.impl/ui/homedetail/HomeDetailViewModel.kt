package com.example.feature.home.impl.ui.homedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.DomainResult
import com.example.feature.home.api.domain.model.HomeItem
import com.example.feature.home.api.domain.usecase.GetHomeItemByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getHomeItemByIdUseCase: GetHomeItemByIdUseCase
) : ViewModel() {

    companion object {
        const val KEY_ITEM_ID = "item_id"
    }

    private val itemId: Int = checkNotNull(savedStateHandle[KEY_ITEM_ID]) {
        "Missing $KEY_ITEM_ID in SavedStateHandle"
    }

    private val _uiEffect = MutableSharedFlow<HomeDetailUiEffect>()
    val uiEffect: SharedFlow<HomeDetailUiEffect> = _uiEffect.asSharedFlow()

    private val retryTrigger = MutableSharedFlow<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HomeDetailUiState> = retryTrigger
        .onStart { emit(Unit) } // Auto-trigger on start
        .flatMapLatest {
            flow {
                emit(HomeDetailUiState.Loading)

                when (val result = getHomeItemByIdUseCase(itemId)) {
                    is DomainResult.Success -> {
                        emit(HomeDetailUiState.Success(result.data))
                    }

                    is DomainResult.Error -> {
                        emit(HomeDetailUiState.Error(result.message))
                        _uiEffect.emit(HomeDetailUiEffect.ShowToast(result.message))
                    }

                    DomainResult.NetworkError -> {
                        val errorMessage = "Network error"
                        emit(HomeDetailUiState.Error(errorMessage))
                        _uiEffect.emit(HomeDetailUiEffect.ShowToast(errorMessage))
                    }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeDetailUiState.Loading
        )

    fun retry() {
        viewModelScope.launch {
            retryTrigger.emit(Unit)
        }
    }
}

sealed interface HomeDetailUiState {
    data object Loading : HomeDetailUiState
    data class Success(val item: HomeItem) : HomeDetailUiState
    data class Error(val message: String) : HomeDetailUiState
}

