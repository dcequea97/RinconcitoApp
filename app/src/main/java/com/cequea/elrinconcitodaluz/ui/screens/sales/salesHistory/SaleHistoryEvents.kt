package com.cequea.elrinconcitodaluz.ui.screens.sales.salesHistory

import com.cequea.elrinconcitodaluz.domain.model.DateOptionSelected
import com.cequea.elrinconcitodaluz.domain.model.Sale

sealed class SaleHistoryEvents{
    data class OnSaleSelected(val sale: Sale): SaleHistoryEvents()
    data class OnDateSelected(val dateOptionSelected: DateOptionSelected): SaleHistoryEvents()
}
