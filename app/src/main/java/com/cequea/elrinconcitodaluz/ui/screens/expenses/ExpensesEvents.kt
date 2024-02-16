package com.cequea.elrinconcitodaluz.ui.screens.expenses

sealed class ExpensesEvents {
    data object OnAddExpenseClicked : ExpensesEvents()
    data object OnErrorMessageHandled : ExpensesEvents()
    data object OnExpenseRegisteredHandled : ExpensesEvents()

    data class OnDescriptionChanged(val description: String) : ExpensesEvents()

    data class OnAmountChanged(val amount: String) : ExpensesEvents()

}