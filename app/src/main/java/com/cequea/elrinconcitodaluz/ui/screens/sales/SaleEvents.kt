package com.cequea.elrinconcitodaluz.ui.screens.sales

import com.cequea.elrinconcitodaluz.domain.model.PaymentStatus
import com.cequea.elrinconcitodaluz.domain.model.Product

sealed class SaleEvents{
    data class OnProductClicked(val product: Product): SaleEvents()

    data class OnPersonNameChanged(val name: String): SaleEvents()

    data class OnPaymentStatusChanged(val status: PaymentStatus): SaleEvents()
    data object OnRegisterSaleClicked: SaleEvents()
    data object OnErrorMessageHandled: SaleEvents()
    data object OnSaleRegisteredHandled: SaleEvents()
}
