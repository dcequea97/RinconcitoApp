package com.cequea.elrinconcitodaluz.ui.screens.inventory

import com.cequea.elrinconcitodaluz.domain.model.PaymentStatus
import com.cequea.elrinconcitodaluz.domain.model.Product

sealed class InventoryEvents{
    data object OnSaveProductClicked: InventoryEvents()
    data object OnErrorMessageHandled: InventoryEvents()
    data object OnSaveProductHandled: InventoryEvents()
    data class OnProductSelected(val product: Product): InventoryEvents()

    data class OnPersonChanged(val string: String): InventoryEvents()
    data class OnCategoryChanged(val string: String): InventoryEvents()
    data class OnPriceChanged(val string: String): InventoryEvents()
    data class OnQuantityChanged(val string: String): InventoryEvents()
    data class OnPercentageDistributionChanged(val key: String, val value: String): InventoryEvents()
}
