package com.cequea.elrinconcitodaluz.ui.screens.debts

import com.cequea.elrinconcitodaluz.domain.model.Debt

data class DebtsUIState(
    val searchBarText: String = "",
    val debts: List<Debt> = listOf()
)