package com.cequea.elrinconcitodaluz.domain.model

data class ProductDebt (
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val total: Double,
    val purchasePrice: Double
)