package com.cequea.elrinconcitodaluz.domain.repository

import com.cequea.elrinconcitodaluz.domain.model.Debt
import com.cequea.elrinconcitodaluz.domain.model.Product
import com.cequea.elrinconcitodaluz.utils.Resource

interface DebtRepository {
    fun getAllDebts(result: (Resource<List<Debt>>) -> Unit)

    fun markDebtAsPaid(debt: Debt, result: (Resource<Boolean>) -> Unit)

    fun makePartialPayment(debt: Debt, amount: Double, result: (Resource<Boolean>) -> Unit)
}