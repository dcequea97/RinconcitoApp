package com.cequea.elrinconcitodaluz.ui.screens.home

import androidx.lifecycle.ViewModel
import com.cequea.elrinconcitodaluz.domain.data.ProductConfigurations
import com.cequea.elrinconcitodaluz.domain.model.Sale
import com.cequea.elrinconcitodaluz.domain.repository.ProductRepository
import com.cequea.elrinconcitodaluz.domain.repository.SaleRepository
import com.cequea.elrinconcitodaluz.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val saleRepository: SaleRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUIState> = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    init {

        val now = LocalDate.now()
        val months = mutableListOf<String>()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM")

        for (i in 0..11) {
            val month = now.minusMonths(i.toLong())
            months.add(month.format(formatter))
        }

        val last12Months = mutableListOf<LocalDate>()

        for (month in months) {
            val date = LocalDate.parse("$month-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            last12Months.add(date)
        }

        _uiState.update { it.copy(
            months = last12Months,
            monthSelected = last12Months.first()
        ) }

        getSalesByMonthSelected()
    }

    fun onEvent(event: HomeEvents) {
        when (event) {
            is HomeEvents.OnMonthSelected -> {
                _uiState.update { it.copy(monthSelected = event.month) }
                getSalesByMonthSelected()
            }
        }
    }
    
    fun getSalesByMonthSelected(){
        _uiState.update { it.copy(isLoading = true) }

        val zoneOffset = ZoneId.systemDefault().rules.getOffset(LocalDateTime.now())
        val dateStart = _uiState.value.monthSelected?.let {
            it.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay()
                .toInstant(zoneOffset).toEpochMilli()
        }
        val dateEnd = _uiState.value.monthSelected?.let {
            it.with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59)
                .toInstant(zoneOffset).toEpochMilli()
        }
        saleRepository.getSalesByDate(
            dateStart = dateStart ?: 0,
            dateEnd = dateEnd ?: 0
        ) { response ->
            when (response) {
                is Resource.Success -> {
                    getCardsInfo(response.data!!)
                    _uiState.update { it.copy(isLoading = false) }
                }

                is Resource.Error -> {
                    _uiState.update { it.copy(errorMessage = response.message, isLoading = false) }
                }
            }
        }
    }

    private fun getCardsInfo(sales: List<Sale>) {
        val configuration = ProductConfigurations.config
        val totalSalesFirstQ = sales
            .filter { sale -> sale.date.toLocalDate().dayOfMonth <= 15 }
            .sumOf { sale -> sale.total }

        val salesByProductFirstQ = sales
            .filter { sale -> sale.date.toLocalDate().dayOfMonth <= 15 }
            .flatMap { it.products }
            .groupBy { it.id }
            .mapValues { (_, list) ->
                list.sumOf { it.total }
            }

        val salesByProductWithConfigurationsFirstQ = HashMap<String, Double>()
        configuration
            .filter { it.productId in salesByProductFirstQ.keys }
            .forEach { config ->
                val currValue = salesByProductWithConfigurationsFirstQ[config.name] ?: 0.0
                val productValue = salesByProductFirstQ[config.productId] ?: 0.0
                salesByProductWithConfigurationsFirstQ[config.name] = currValue.plus(
                    productValue * (config.value / 100)
                )
            }

        val totalSalesSecondQ = sales
            .filter { sale -> sale.date.toLocalDate().dayOfMonth > 15 }
            .sumOf { sale -> sale.total }

        val salesByProductSecondQ = sales
            .filter { sale -> sale.date.toLocalDate().dayOfMonth > 15 }
            .flatMap { it.products }
            .groupBy { it.id }
            .mapValues { (_, list) ->
                list.sumOf { it.total }
            }

        val salesByProductWithConfigurationsSecondQ = HashMap<String, Double>()
        configuration
            .filter { it.productId in salesByProductSecondQ.keys }
            .forEach { config ->
                val currValue = salesByProductWithConfigurationsSecondQ[config.name] ?: 0.0
                val productValue = salesByProductSecondQ[config.productId] ?: 0.0
                salesByProductWithConfigurationsSecondQ[config.name] = currValue.plus(
                    productValue * (config.value / 100)
                )
            }

        val totalIncomesFirstQ = sales
            .filter {
                it.date.toLocalDate().dayOfMonth <= 15
            }
            .sumOf { sale ->
                sale.products
                    .sumOf { product -> (product.price - product.purchasePrice) * product.quantity }
            }

        val totalIncomesSecondQ = sales
            .filter {
                it.date.toLocalDate().dayOfMonth > 15
            }
            .sumOf { sale ->
                sale.products
                    .sumOf { product -> (product.price - product.purchasePrice) * product.quantity }
            }


        _uiState.update {
            it.copy(
                salesByProductFirstQ = salesByProductWithConfigurationsFirstQ,
                salesByProductSecondQ = salesByProductWithConfigurationsSecondQ,
                totalSalesFirstQ = totalSalesFirstQ,
                totalSalesSecondQ = totalSalesSecondQ,
                totalIncomesFirstQ = totalIncomesFirstQ,
                totalExpensesSecondQ = totalIncomesSecondQ
            )
        }
    }

}