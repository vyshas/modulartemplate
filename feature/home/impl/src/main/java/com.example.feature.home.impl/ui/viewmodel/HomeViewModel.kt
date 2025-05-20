package com.example.feature.home.impl.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.home.api.domain.usecase.GetHomeItemsUseCase
import kotlinx.coroutines.launch

class HomeViewModel(private val useCase: GetHomeItemsUseCase) : ViewModel() {
    init {
        viewModelScope.launch {
            useCase.getHomeItems()
        }
    }
}
