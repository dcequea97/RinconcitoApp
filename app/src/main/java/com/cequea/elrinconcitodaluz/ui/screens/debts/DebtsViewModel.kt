package com.cequea.elrinconcitodaluz.ui.screens.debts

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DebtsViewModel @Inject constructor(): ViewModel() {

    private val _uiState: MutableStateFlow<DebtsUIState> = MutableStateFlow(
        DebtsUIState()
    )
    val uiState: StateFlow<DebtsUIState> = _uiState.asStateFlow()

    fun onEvent(event: DebtsEvents) {
        when(event) {
            is DebtsEvents.OnQueryChanged -> {
                _uiState.value = _uiState.value.copy(
                    searchBarText = event.newQuery
                )
            }

            is DebtsEvents.OnDebtClicked -> {

            }
        }
    }
}