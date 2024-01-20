package com.cequea.elrinconcitodaluz.ui.screens.sales

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SaleViewModel @Inject constructor(): ViewModel() {

    private val _uiState: MutableStateFlow<SaleUIState> = MutableStateFlow(
        SaleUIState()
    )
    val uiState: StateFlow<SaleUIState> = _uiState.asStateFlow()

    fun onEvent(event: SaleEvents) {
//        when(event) {
//
//        }
    }
}