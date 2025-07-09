package com.example.feature.home.impl.ui.homedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.DomainResult
import com.example.feature.home.api.domain.model.HomeItem
import com.example.feature.home.api.domain.usecase.GetHomeItemByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _mutableUiState = MutableStateFlow<HomeDetailUiState>(HomeDetailUiState.Loading)
    val uiState: StateFlow<HomeDetailUiState> = _mutableUiState.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeDetailUiState.Loading
    )

    private val _uiEffect = MutableSharedFlow<HomeDetailUiEffect>()
    val uiEffect: SharedFlow<HomeDetailUiEffect> = _uiEffect.asSharedFlow()

    init {
        val itemId: Int = checkNotNull(savedStateHandle[KEY_ITEM_ID]) {
            "Missing $KEY_ITEM_ID in SavedStateHandle"
        }
        onIntent(HomeDetailUiIntent.FetchItem(itemId))
    }

    fun onIntent(intent: HomeDetailUiIntent) {
        when (intent) {
            is HomeDetailUiIntent.FetchItem -> {
                fetchHomeItem(intent.itemId)
            }
        }
    }

    private fun fetchHomeItem(itemId: Int) {
        viewModelScope.launch {
            _mutableUiState.value = HomeDetailUiState.Loading
            when (val result = getHomeItemByIdUseCase(itemId)) {
                is DomainResult.Success -> _mutableUiState.value =
                    HomeDetailUiState.Success(result.data)

                is DomainResult.Error -> {
                    _mutableUiState.value = HomeDetailUiState.Error(result.message)
                    _uiEffect.emit(HomeDetailUiEffect.ShowToast(result.message))
                }

                DomainResult.NetworkError -> {
                    _mutableUiState.value = HomeDetailUiState.Error("Network error")
                    _uiEffect.emit(HomeDetailUiEffect.ShowToast("Network error"))
                }
            }
        }
    }
}

sealed interface HomeDetailUiState {
    data object Loading : HomeDetailUiState
    data class Success(val item: HomeItem) : HomeDetailUiState
    data class Error(val message: String) : HomeDetailUiState
}

