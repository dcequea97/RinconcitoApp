package com.cequea.elrinconcitodaluz.domain.repository

import com.cequea.elrinconcitodaluz.domain.model.Product
import com.cequea.elrinconcitodaluz.domain.model.Sale
import com.cequea.elrinconcitodaluz.utils.Resource

interface SaleRepository {
    fun getAllSales(result: (Resource<List<Sale>>) -> Unit)

    fun getSalesByDate(dateStart: Long, dateEnd: Long, result: (Resource<List<Sale>>) -> Unit)

    fun registerSale(
        personName: String,
        products: List<Product>,
        paymentStatus: Int,
        result: (Resource<Boolean>) -> Unit
    )
}