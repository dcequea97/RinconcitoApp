package com.cequea.elrinconcitodaluz.ui.screens.sales.salesHistory

import com.cequea.elrinconcitodaluz.domain.model.DateOptionSelected
import com.cequea.elrinconcitodaluz.domain.model.Sale

data class SalesHistoryUIState(
    val sales: List<Sale> = emptyList(),
    val saleSelected : Sale? = null,
    val dateOptionSelected: DateOptionSelected? = null,
    val dateOptions: List<DateOptionSelected> = emptyList()
)