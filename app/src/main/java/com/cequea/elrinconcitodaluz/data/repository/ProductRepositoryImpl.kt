package com.cequea.elrinconcitodaluz.data.repository

import com.cequea.elrinconcitodaluz.domain.model.PercentageConfig
import com.cequea.elrinconcitodaluz.domain.model.Product
import com.cequea.elrinconcitodaluz.domain.repository.ProductRepository
import com.cequea.elrinconcitodaluz.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): ProductRepository {
    override fun getAllProducts( result: (Resource<List<Product>> ) -> Unit) {
        db.collection("products")
        .get()
        .addOnSuccessListener { query ->
            val products = arrayListOf<Product?>()
            for (product in query){
                try {
                    val percentageDistribution = product.get("percentageDistribution") as Map<String, Any>? ?: hashMapOf()
                    val percentageDistributionDouble = percentageDistribution.mapValues { (_, value) ->
                        when (value) {
                            is Double -> value
                            is Long -> value.toDouble()
                            is Int -> value.toDouble()
                            else -> 0.0
                        }
                    }

                    val mProduct = Product(
                        id = product.id,
                        name = product.getString("name") ?: "",
                        imageUrl = product.getString("imageUrl") ?: "",
                        price = product.getDouble("price") ?: 0.0,
                        description = product.getString("description") ?: "",
                        category = product.getString("category") ?: "",
                        cartQuantity = 0,
                        percentageDistribution = percentageDistributionDouble,
                        stock = product.getLong("stock")?.toInt() ?: 0,
                        purchasePrice = product.getDouble("purchasePrice") ?: 0.0
                    )
                    products.add(mProduct)
                    Timber.i("Product: $mProduct")
                } catch (e: Exception) {
                    Timber.e("Error mapping product: ${product.id}", e)
                }
            }
            result.invoke(Resource.Success(products.filterNotNull()))
        }
        .addOnFailureListener { exception ->
            result.invoke(
                Resource.Error(
                    exception.localizedMessage ?: "An unexpected error occurred"
                )
            )
            Timber.e("Error getting products.", exception)
        }
    }

    override fun updateProduct(product: Product, result: (Resource<Boolean>) -> Unit) {
        db.collection("products")
        .document(product.id)
        .set(product)
        .addOnSuccessListener {
            result.invoke(Resource.Success(true))
        }
        .addOnFailureListener { exception ->
            result.invoke(
                Resource.Error(
                    exception.localizedMessage ?: "An unexpected error occurred"
                )
            )
            Timber.e("Error updating product.", exception)
        }
    }

    override fun getAllProductsConfigurations(result: (Resource<List<PercentageConfig>>) -> Unit) {
        db.collection("percentageConfig")
        .get()
        .addOnSuccessListener { query ->
            val percentageConfig = arrayListOf<PercentageConfig>()
            for (config in query){
                try {
                    val mConfig = PercentageConfig(
                        percentageId = config.id,
                        name = config.getString("name") ?: "",
                        value = config.getDouble("value") ?: 0.0,
                        productId = config.getString("productId") ?: ""
                    )
                    percentageConfig.add(mConfig)
                    Timber.i("PercentageConfig: $mConfig")
                } catch (e: Exception) {
                    Timber.e("Error mapping percentageConfig: ${config.id}", e)
                }
            }
            result.invoke(Resource.Success(percentageConfig))
        }
        .addOnFailureListener { exception ->
            result.invoke(
                Resource.Error(
                    exception.localizedMessage ?: "An unexpected error occurred"
                )
            )
            Timber.e("Error getting percentageConfig.", exception)
        }
    }
}