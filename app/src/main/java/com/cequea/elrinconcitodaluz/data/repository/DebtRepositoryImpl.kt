package com.cequea.elrinconcitodaluz.data.repository

import com.cequea.elrinconcitodaluz.common.getDateFromLong
import com.cequea.elrinconcitodaluz.domain.model.Debt
import com.cequea.elrinconcitodaluz.domain.model.ProductDebt
import com.cequea.elrinconcitodaluz.domain.repository.DebtRepository
import com.cequea.elrinconcitodaluz.utils.Resource
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import javax.inject.Inject

class DebtRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): DebtRepository {
    override fun getAllDebts(result: (Resource<List<Debt>>) -> Unit) {
        db.collection("debts")
            .whereEqualTo("visible", true)
            .get()
        .addOnSuccessListener { query ->
            val debts = arrayListOf<Debt?>()
            for (debt in query){
                try {
                    val productMapList = debt.get("products") as List<HashMap<String, Any>>
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

                    val mDebt = Debt(
                        id = debt.id,
                        personName = debt.getString("personName") ?: "",
                        amount = debt.getDouble("total") ?: 0.0,
                        products = productList,
                        date = debt.getLong("date").getDateFromLong(),
                        isPaid = false
                    )
                    debts.add(mDebt)
                    Timber.i("Debt: $mDebt")
                } catch (e: Exception) {
                    Timber.e("Error mapping debt: ${debt.id}", e)
                }
            }
            result.invoke(Resource.Success(debts.filterNotNull()))
        }
        .addOnFailureListener { exception ->
            result.invoke(
                Resource.Error(
                    exception.localizedMessage ?: "An unexpected error occurred"
                )
            )
            Timber.e("Error getting debts.", exception)
        }
    }

    override fun markDebtAsPaid(debt: Debt, result: (Resource<Boolean>) -> Unit) {
        db.runTransaction { transaction ->
            val debtRef = db.collection("debts").document(debt.id)
            transaction.update(debtRef, "visible", false)

            val sale = hashMapOf(
                "personName" to debt.personName,
                "products" to debt.products.map { product ->
                    hashMapOf(
                        "id" to product.id,
                        "name" to product.name,
                        "price" to product.price,
                        "quantity" to product.quantity,
                        "total" to product.total,
                        "purchasePrice" to product.purchasePrice
                    )
                },
                "date" to System.currentTimeMillis(),
                "total" to debt.amount,
                "paymentStatus" to 2
            )

            val balanceRef = db.collection("balances").document("current")
            transaction.update(balanceRef, "capital", FieldValue.increment(debt.amount))

            transaction.set(db.collection("sales").document(), sale)
        }.addOnSuccessListener {
            result.invoke(Resource.Success(true))
        }.addOnFailureListener { exception ->
            result.invoke(
                Resource.Error(
                    exception.localizedMessage ?: "An unexpected error occurred"
                )
            )
            Timber.e("Error in transaction.", exception)
        }

    }

    override fun makePartialPayment(
        debt: Debt,
        amount: Double,
        result: (Resource<Boolean>) -> Unit
    ) {
        db.runTransaction { transaction ->
            val debtRef = db.collection("debts").document(debt.id)
            val newAmount = debt.amount - amount
            transaction.update(debtRef, "total", newAmount)

            var amountTemp = amount
            var products = debt.products.toMutableList()

            val sale = hashMapOf(
                "personName" to debt.personName,
                "products" to debt.products.mapIndexedNotNull  { index, product ->
                    if (amountTemp <= 0) return@mapIndexedNotNull null
                    val newTotal = if (amountTemp >= product.total) {
                        product.total
                    } else {
                        amountTemp
                    }
                    val quantity = if (amountTemp >= product.price * product.quantity) {
                        products.removeAt(index)
                        transaction.update(debtRef, "products", products)
                        product.quantity
                    } else {
                        val newQuantity = product.quantity - (amountTemp / product.price).toInt()
                        products[index] = product.copy(
                            quantity = newQuantity,
                            total = product.total - (newQuantity * product.price)
                        )
                        transaction.update(debtRef, "products", products)
                        (amountTemp / product.price).toInt()
                    }

                    amountTemp -= product.total

                    product.copy(total = newTotal)

                    hashMapOf(
                        "id" to product.id,
                        "name" to product.name,
                        "price" to product.price,
                        "quantity" to quantity,
                        "total" to newTotal,
                        "purchasePrice" to product.purchasePrice
                    )
                },
                "date" to System.currentTimeMillis(),
                "total" to amount,
                "paymentStatus" to 2
            )

            val balanceRef = db.collection("balances").document("current")
            transaction.update(balanceRef, "capital", FieldValue.increment(amount))

            transaction.set(db.collection("sales").document(), sale)
        }.addOnSuccessListener {
            result.invoke(Resource.Success(true))
        }.addOnFailureListener { exception ->
            result.invoke(
                Resource.Error(
                    exception.localizedMessage ?: "An unexpected error occurred"
                )
            )
            Timber.e("Error in transaction.", exception)
        }
    }


}