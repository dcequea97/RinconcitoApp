package com.cequea.elrinconcitodaluz.ui.screens.home

import java.time.LocalDate
import java.time.Month

data class HomeUIState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val months2: List<Month> = emptyList(),
    val months: List<LocalDate> = emptyList(),
    val monthSelected: LocalDate? = null,

    val salesByProductFirstQ: Map<String, Double> = hashMapOf(),
    val salesByProductSecondQ: Map<String, Double> = hashMapOf(),

    val totalSalesFirstQ: Double = 0.0,
    val totalIncomesFirstQ: Double = 0.0,
    val totalExpensesFirstQ: Double = 0.0,

    val totalSalesSecondQ: Double = 0.0,
    val totalIncomesSecondQ: Double = 0.0,
    val totalExpensesSecondQ: Double = 0.0,
)
