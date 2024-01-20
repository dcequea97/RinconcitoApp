package com.cequea.elrinconcitodaluz.domain.model

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val description: String,
    val image: String,
    val category: String,
    val stock: Int
)
