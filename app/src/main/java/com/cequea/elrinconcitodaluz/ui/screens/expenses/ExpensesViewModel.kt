package com.cequea.elrinconcitodaluz.ui.screens.expenses

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(): ViewModel() {

    private val _uiState: MutableStateFlow<ExpensesUIState> = MutableStateFlow(ExpensesUIState())
    val uiState: StateFlow<ExpensesUIState> = _uiState.asStateFlow()

    fun onEvent(event: ExpensesEvents) {
        when (event) {
            is ExpensesEvents.OnAddExpenseClicked -> {

            }

            is ExpensesEvents.OnErrorMessageHandled -> {
                _uiState.value = _uiState.value.copy(errorMessage = null)
            }

            is ExpensesEvents.OnExpenseRegisteredHandled -> {
                _uiState.value = _uiState.value.copy(expenseRegistered = false)
            }

            is ExpensesEvents.OnDescriptionChanged -> {
                _uiState.value = _uiState.value.copy(description = event.description)
            }

            is ExpensesEvents.OnAmountChanged -> {
                _uiState.value = _uiState.value.copy(amount = event.amount)
            }
        }
    }
}