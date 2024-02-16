package com.cequea.elrinconcitodaluz.domain.repository

import com.cequea.elrinconcitodaluz.domain.model.PercentageConfig
import com.cequea.elrinconcitodaluz.domain.model.Product
import com.cequea.elrinconcitodaluz.utils.Resource

interface ProductRepository {
    fun getAllProducts(result: (Resource<List<Product>>) -> Unit)

    fun updateProduct(product: Product, result: (Resource<Boolean>) -> Unit)

    fun getAllProductsConfigurations(result: (Resource<List<PercentageConfig>>) -> Unit)
}