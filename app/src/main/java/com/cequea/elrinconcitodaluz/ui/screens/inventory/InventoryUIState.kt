package com.cequea.elrinconcitodaluz.ui.screens.inventory

import com.cequea.elrinconcitodaluz.domain.model.Debt
import com.cequea.elrinconcitodaluz.domain.model.PaymentStatus
import com.cequea.elrinconcitodaluz.domain.model.Product

data class InventoryUIState(
    val products: List<Product> = emptyList(),
    val productSelected: Product? = null,
    val productSaved: Boolean = false,

    val name: String = "",
    val category: String = "",
    val price: String = "",
    val quantity: String = "",
    val percentageDistribution: Map<String, Double> = hashMapOf(),
)