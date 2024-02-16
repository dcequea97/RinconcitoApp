package com.cequea.elrinconcitodaluz.domain.model

data class Product(
    val id: String,
    val name: String,
    val imageUrl: String,
    val price: Double,
    val description: String,
    val category: String,
    val cartQuantity: Int,
    val percentageDistribution: Map<String, Double> = hashMapOf(),
    val stock: Int,
    val purchasePrice: Double
)