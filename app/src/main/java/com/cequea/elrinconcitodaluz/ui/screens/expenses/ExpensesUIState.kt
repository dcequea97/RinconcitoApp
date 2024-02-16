package com.cequea.elrinconcitodaluz.ui.screens.expenses

data class ExpensesUIState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val amount: String = "",
    val description: String = "",
    val expenseRegistered: Boolean = false
)
