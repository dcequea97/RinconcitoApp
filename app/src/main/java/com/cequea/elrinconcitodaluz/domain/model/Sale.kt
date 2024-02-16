package com.cequea.elrinconcitodaluz.domain.model

import java.time.LocalDateTime

data class Sale(
    val id: String,
    val products: List<ProductDebt>,
    val personName: String,
    val total: Double,
    val date: LocalDateTime
)
