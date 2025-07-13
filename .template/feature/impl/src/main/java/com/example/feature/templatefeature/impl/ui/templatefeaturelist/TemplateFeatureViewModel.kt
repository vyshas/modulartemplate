package com.example.feature.templatefeature.impl.ui.templatefeaturelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.coroutines.DispatcherProvider
import com.example.core.domain.DomainResult
import com.example.feature.templatefeature.api.domain.usecase.GetTemplateFeaturesUseCase
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
class TemplateFeatureViewModel @Inject constructor(
    private val getTemplateFeaturesUseCase: GetTemplateFeaturesUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiEffect = MutableSharedFlow<TemplateFeatureUiEffect>()
    val uiEffect: SharedFlow<TemplateFeatureUiEffect> = _uiEffect.asSharedFlow()

    private val refreshTrigger = MutableSharedFlow<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<TemplateFeatureUiState> = refreshTrigger
        .onStart { emit(Unit) } // Auto-trigger on start
        .flatMapLatest {
            flow {
                emit(TemplateFeatureUiState.Loading)
                when (val result = getTemplateFeaturesUseCase.getTemplateFeatures()) {
                    is DomainResult.Success -> emit(TemplateFeatureUiState.Success(result.data))
                    is DomainResult.Error -> emit(TemplateFeatureUiState.Error(result.message))
                    DomainResult.NetworkError -> emit(TemplateFeatureUiState.Error("Network error"))
                }
            }.flowOn(dispatcherProvider.io())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TemplateFeatureUiState.Loading
        )

    fun onIntent(intent: TemplateFeatureUiIntent) {
        when (intent) {
            TemplateFeatureUiIntent.FetchTemplateFeatures -> refresh()
            is TemplateFeatureUiIntent.TemplateFeatureClicked -> onTemplateFeatureClicked(intent.templatefeatureId)
        }
    }

    private fun onTemplateFeatureClicked(templatefeatureId: Int) {
        viewModelScope.launch {
            _uiEffect.emit(TemplateFeatureUiEffect.NavigateToTemplateFeatureDetail(templatefeatureId))
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            refreshTrigger.emit(Unit)
        }
    }
}