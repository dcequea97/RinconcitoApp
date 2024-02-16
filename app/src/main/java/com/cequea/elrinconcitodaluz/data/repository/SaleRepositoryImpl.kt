package com.cequea.elrinconcitodaluz.data.repository

import com.cequea.elrinconcitodaluz.common.getDateFromLong
import com.cequea.elrinconcitodaluz.domain.data.ProductConfigurations
import com.cequea.elrinconcitodaluz.domain.model.Product
import com.cequea.elrinconcitodaluz.domain.model.ProductDebt
import com.cequea.elrinconcitodaluz.domain.model.Sale
import com.cequea.elrinconcitodaluz.domain.repository.ProductRepository
import com.cequea.elrinconcitodaluz.domain.repository.SaleRepository
import com.cequea.elrinconcitodaluz.utils.Resource
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

class SaleRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): SaleRepository {
    override fun getAllSales(result: (Resource<List<Sale>>) -> Unit) {
        db.collection("sales")
            .get()
            .addOnSuccessListener { query ->
                val sales = arrayListOf<Sale>()
                for (sale in query){
                    try {
                        val productMapList = sale.get("products") as List<HashMap<String, Any>>
                        val productList = productMapList.map { productMap ->
                            ProductDebt(
                                id = productMap["id"] as String,
                                name = productMap["name"] as String,
                                price = (productMap["price"] as Number).toDouble(),
                                quantity = (productMap["quantity"] as Long).toInt(),
                                total = (productMap["total"] as Number).toDouble(),
                                purchasePrice = (productMap["purchasePrice"] as Number).toDouble()
                            )
                        }

                        val mSale = Sale(
                            id = sale.id,
                            products = productList,
                            date = sale.getLong("date").getDateFromLong(),
                            personName = sale.getString("personName") ?: "",
                            total = sale.getDouble("total") ?: 0.0
                        )
                        sales.add(mSale)
                        Timber.i("Sale: $mSale")
                    } catch (e: Exception) {
                        Timber.e("Error mapping sale: ${sale.id}", e)
                    }
                }
                result.invoke(Resource.Success(sales))
            }
            .addOnFailureListener { exception ->
                result.invoke(
                    Resource.Error(
                        exception.localizedMessage ?: "An unexpected error occurred"
                    )
                )
                Timber.e("Error getting sales.", exception)
            }
    }

    override fun getSalesByDate(
        dateStart: Long,
        dateEnd: Long,
        result: (Resource<List<Sale>>) -> Unit
    ) {
        db.collection("sales")
            .whereGreaterThanOrEqualTo("date", dateStart)
            .whereLessThanOrEqualTo("date", dateEnd)
            .get()
            .addOnSuccessListener { query ->
                val sales = arrayListOf<Sale>()
                for (sale in query){
                    try {
                        val productMapList = sale.get("products") as List<HashMap<String, Any>>
                        val productList = productMapList.map { productMap ->
                            ProductDebt(
                                id = productMap["id"] as String,
                                name = productMap["name"] as String,
                                price = (productMap["price"] as Number).toDouble(),
                                quantity = (productMap["quantity"] as Long).toInt(),
                                total = (productMap["total"] as Number).toDouble(),
                                purchasePrice = (productMap["purchasePrice"] as Number).toDouble()
                            )
                        }

                        val mSale = Sale(
                            id = sale.id,
                            products = productList,
                            date = sale.getLong("date").getDateFromLong(),
                            personName = sale.getString("personName") ?: "",
                            total = sale.getDouble("total") ?: 0.0
                        )
                        sales.add(mSale)
                        Timber.i("Sale: $mSale")
                    } catch (e: Exception) {
                        Timber.e("Error mapping sale: ${sale.id}", e)
                    }
                }
                result.invoke(Resource.Success(sales))
            }
            .addOnFailureListener { exception ->
                result.invoke(
                    Resource.Error(
                        exception.localizedMessage ?: "An unexpected error occurred"
                    )
                )
                Timber.e("Error getting sales.", exception)
            }
    }


    override fun registerSale(
        personName: String,
        products: List<Product>,
        paymentStatus: Int,
        result: (Resource<Boolean>) -> Unit
    ) {
        val mProducts = products.map { product ->
            hashMapOf(
                "id" to product.id,
                "name" to product.name,
                "price" to product.price,
                "quantity" to product.cartQuantity,
                "total" to product.cartQuantity * product.price,
                "purchasePrice" to product.purchasePrice
            )
        }

        val totalSale = products.sumOf { it.cartQuantity * it.price }
        val sale = hashMapOf(
            "personName" to personName,
            "products" to mProducts,
            "paymentStatus" to paymentStatus,
            "date" to System.currentTimeMillis(),
            "total" to totalSale
        )

        if (paymentStatus == 1) {
            sale["visible"] = true
        }
        val collectionName = if (paymentStatus == 1) "debts" else "sales"


        val batch = db.batch()

        mProducts.forEach { product ->
            val productRef = db.collection("products").document(product["id"] as String)

            val quantity = product["quantity"] as Int
            batch.update(productRef, "stock", FieldValue.increment(-quantity.toLong()))
        }

        batch.set(db.collection(collectionName).document(), sale)
        if (paymentStatus != 1) {
            val balanceRef = db.collection("balances").document("current")
            batch.update(balanceRef, "capital", FieldValue.increment(totalSale))
        }

        batch.commit().addOnSuccessListener {
            result.invoke(Resource.Success(true))
        }.addOnFailureListener { exception ->
            result.invoke(
                Resource.Error(
                    exception.localizedMessage ?: "An unexpected error occurred"
                )
            )
            Timber.e("Error registering sale.", exception)
        }
    }
}