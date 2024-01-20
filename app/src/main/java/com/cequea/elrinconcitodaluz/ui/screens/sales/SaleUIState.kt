package com.cequea.elrinconcitodaluz.ui.screens.sales

import com.cequea.elrinconcitodaluz.domain.model.Debt
import com.cequea.elrinconcitodaluz.domain.model.Product

data class SaleUIState(
    val products: List<Product> = emptyList(),
    val productSelected: Product? = null,
    val personName: String = "",
    val quantity: String = "",
    val total: String = "",

)