package com.example.feature.recipe.impl.ui.recipelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.coroutines.DispatcherProvider
import com.example.core.domain.DomainResult
import com.example.feature.recipe.api.domain.usecase.GetRecipesUseCase
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
class RecipeViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiEffect = MutableSharedFlow<RecipeUiEffect>()
    val uiEffect: SharedFlow<RecipeUiEffect> = _uiEffect.asSharedFlow()

    private val refreshTrigger = MutableSharedFlow<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<RecipeUiState> = refreshTrigger
        .onStart { emit(Unit) } // Auto-trigger on start
        .flatMapLatest {
            flow {
                emit(RecipeUiState.Loading)
                when (val result = getRecipesUseCase.getRecipes()) {
                    is DomainResult.Success -> emit(RecipeUiState.Success(result.data))
                    is DomainResult.Error -> emit(RecipeUiState.Error(result.message))
                    DomainResult.NetworkError -> emit(RecipeUiState.Error("Network error"))
                }
            }.flowOn(dispatcherProvider.io())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RecipeUiState.Loading,
        )

    /*    @OptIn(ExperimentalCoroutinesApi::class)
        val uiState: StateFlow<PagingData<Recipe>> = refreshTrigger
            .onStart { emit(Unit) } // Auto-trigger on start
            .flatMapLatest {
                getPagedRecipesUseCase.getPagedRecipes()
            }.cachedIn(viewModelScope)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = PagingData.empty(),
            )*/

    fun onIntent(intent: RecipeUiIntent) {
        when (intent) {
            RecipeUiIntent.FetchRecipes -> refresh()
            is RecipeUiIntent.RecipeClicked -> onRecipeClicked(intent.recipeId)
        }
    }

    private fun onRecipeClicked(recipeId: Int) {
        viewModelScope.launch {
            _uiEffect.emit(RecipeUiEffect.NavigateToRecipeDetail(recipeId))
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            refreshTrigger.emit(Unit)
        }
    }
}
