package com.cequea.elrinconcitodaluz.ui.screens.sales

import com.cequea.elrinconcitodaluz.domain.model.Debt
import com.cequea.elrinconcitodaluz.domain.model.PaymentStatus
import com.cequea.elrinconcitodaluz.domain.model.Product

data class SaleUIState(
    val products: List<Product> = emptyList(),
    val personName: String = "",
    val quantity: String = "",
    val total: String = "",

    val paymentStatus: List<PaymentStatus> = listOf(
        PaymentStatus(1, "Pendiente"),
        PaymentStatus(2, "Pagado"),
    ),
    val paymentStatusSelected: PaymentStatus = PaymentStatus(1, "Pendiente"),

    val saleRegistered: Boolean = false,
)