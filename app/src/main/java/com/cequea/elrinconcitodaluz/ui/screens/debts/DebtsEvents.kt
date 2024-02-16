package com.cequea.elrinconcitodaluz.ui.screens.debts

import com.cequea.elrinconcitodaluz.domain.model.Debt
import com.cequea.elrinconcitodaluz.ui.screens.sales.SaleEvents

sealed class DebtsEvents{
    data class OnQueryChanged(val newQuery: String): DebtsEvents()
    data class OnDebtClicked(val debt: Debt): DebtsEvents()
    data class OnPayPartiallyAmountChanged(val amount: String): DebtsEvents()
    data object OnPayPartiallyShowClicked: DebtsEvents()
    data object OnPayPartiallyClicked: DebtsEvents()


    data class OnMarkAsPaidClicked(val debt: Debt): DebtsEvents()
    data object OnDebtMarkedAsPaidHandled: DebtsEvents()
}
