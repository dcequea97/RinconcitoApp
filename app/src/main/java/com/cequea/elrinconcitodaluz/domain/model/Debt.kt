package com.cequea.elrinconcitodaluz.domain.model

import java.time.LocalDateTime

data class Debt(
    val id: String,
    val personName: String,
    val products: List<ProductDebt>,
    val date: LocalDateTime,
    val isPaid: Boolean,
    val amount: Double
){
    fun getListOfProducts(): String{
        var listOfProducts = ""
        for (product in products){
            listOfProducts += product.name + ", "
        }
        return listOfProducts.dropLast(2)
    }

    fun getTotalAmount(): Double{
        var totalAmount = 0.0
        for (product in products){
            totalAmount += product.price * product.quantity
        }
        return totalAmount
    }
}