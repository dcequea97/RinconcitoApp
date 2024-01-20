package com.cequea.elrinconcitodaluz.ui.screens.debts

sealed class DebtsEvents{
    data class OnQueryChanged(val newQuery: String): DebtsEvents()
    data class OnDebtClicked(val debtId: String): DebtsEvents()
}
