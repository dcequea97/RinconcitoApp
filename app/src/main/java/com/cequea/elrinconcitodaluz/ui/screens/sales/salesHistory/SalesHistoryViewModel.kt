package com.cequea.elrinconcitodaluz.ui.screens.sales.salesHistory

import androidx.lifecycle.ViewModel
import com.cequea.elrinconcitodaluz.domain.model.DateOptionSelected
import com.cequea.elrinconcitodaluz.domain.repository.ProductRepository
import com.cequea.elrinconcitodaluz.domain.repository.SaleRepository
import com.cequea.elrinconcitodaluz.ui.screens.sales.SaleEvents
import com.cequea.elrinconcitodaluz.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class SalesHistoryViewModel @Inject constructor(
    private val saleRepository: SaleRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<SalesHistoryUIState> =
        MutableStateFlow(SalesHistoryUIState())
    val uiState: StateFlow<SalesHistoryUIState> = _uiState.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        _uiState.update { uiState ->
            uiState.copy(
                dateOptions = listOf(
                    DateOptionSelected(0, "Todas"),
                    DateOptionSelected(1, "Hoy"),
                    DateOptionSelected(2, "Ayer"),
                    DateOptionSelected(3, "Esta semana"),
                    DateOptionSelected(4, "Esta quincena"),
                    DateOptionSelected(5, "Este Mes"),
                ),
                dateOptionSelected = DateOptionSelected(1, "Hoy")
            )
        }


        _isLoading.update { true }

        getSalesByDateOption(DateOptionSelected(1, "Hoy"))
    }

    fun onEvent(event: SaleHistoryEvents) {
        when (event) {
            is SaleHistoryEvents.OnSaleSelected -> {
                _uiState.update { uiState ->
                    uiState.copy(saleSelected = event.sale)
                }
            }

            is SaleHistoryEvents.OnDateSelected -> {
                _uiState.update { uiState ->
                    uiState.copy(dateOptionSelected = event.dateOptionSelected)
                }

                getSalesByDateOption(event.dateOptionSelected)
            }

        }
    }

    fun getSalesByDateOption(dateOption: DateOptionSelected) {
        _isLoading.update { true }
        val zoneOffset = ZoneId.systemDefault().rules.getOffset(LocalDateTime.now())
        val dateStart = when (dateOption.id) {
            1 -> LocalDate.now().atStartOfDay().toInstant(zoneOffset).toEpochMilli()
            2 -> LocalDate.now().minusDays(1).atStartOfDay().toInstant(zoneOffset).toEpochMilli()
            3 -> LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay().toInstant(zoneOffset).toEpochMilli()
            4 -> LocalDate.now().withDayOfMonth(1).atStartOfDay().toInstant(zoneOffset).toEpochMilli()
            5 -> LocalDate.now().withDayOfMonth(1).atStartOfDay().toInstant(zoneOffset).toEpochMilli()
            else -> null
        }

        val dateEnd = when (dateOption.id) {
            1 -> LocalDate.now().atTime(23, 59, 59).toInstant(zoneOffset).toEpochMilli()
            2 -> LocalDate.now().minusDays(1).atTime(23, 59, 59).toInstant(zoneOffset).toEpochMilli()
            3 -> LocalDate.now().with(DayOfWeek.SUNDAY).atTime(23, 59, 59).toInstant(zoneOffset).toEpochMilli()
            4 -> LocalDate.now().withDayOfMonth(15).atTime(23, 59, 59).toInstant(zoneOffset).toEpochMilli()
            5 -> LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59).toInstant(zoneOffset).toEpochMilli()
            else -> null
        }

        saleRepository.getSalesByDate(dateStart = dateStart ?: 0, dateEnd = dateEnd ?: 0) { response ->
            when (response) {
                is Resource.Success -> {
                    _uiState.update { it.copy(sales = response.data!!) }
                    _isLoading.update { false }
                }

                is Resource.Error -> {
                    _errorMessage.update { response.message }
                    _isLoading.update { false }
                }
            }
        }
    }
}