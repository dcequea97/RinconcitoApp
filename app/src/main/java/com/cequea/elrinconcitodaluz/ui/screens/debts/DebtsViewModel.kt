package com.cequea.elrinconcitodaluz.ui.screens.debts

import androidx.lifecycle.ViewModel
import com.cequea.elrinconcitodaluz.domain.model.Debt
import com.cequea.elrinconcitodaluz.domain.repository.DebtRepository
import com.cequea.elrinconcitodaluz.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DebtsViewModel @Inject constructor(
    private val debtRepository: DebtRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<DebtsUIState> = MutableStateFlow(
        DebtsUIState()
    )
    val uiState: StateFlow<DebtsUIState> = _uiState.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        getAllDebts()
    }

    fun getAllDebts() {
        _isLoading.update { true }
        debtRepository.getAllDebts { response ->
            when (response) {
                is Resource.Success -> {
                    _uiState.update { it.copy(debts = response.data!!) }
                    _isLoading.update { false }
                }

                is Resource.Error -> {
                    _errorMessage.update { response.message }
                    _isLoading.update { false }
                }
            }
        }
    }

    fun onEvent(event: DebtsEvents) {
        when (event) {
            is DebtsEvents.OnQueryChanged -> {
                _uiState.update { _uiState.value.copy(searchBarText = event.newQuery) }
            }

            is DebtsEvents.OnDebtClicked -> {
                _uiState.update { _uiState.value.copy(debtSelected = event.debt) }
            }

            is DebtsEvents.OnMarkAsPaidClicked -> {
                markDebtAsPaid(event.debt)
            }

            is DebtsEvents.OnDebtMarkedAsPaidHandled -> {
                _uiState.update { it.copy(onDebtMarkedAsPaid = false) }
            }

            is DebtsEvents.OnPayPartiallyAmountChanged -> {
                _uiState.update { it.copy(payPartiallyAmount = event.amount) }
            }

            is DebtsEvents.OnPayPartiallyShowClicked -> {
                _uiState.update { it.copy(payPartiallyShow = !it.payPartiallyShow) }
            }

            is DebtsEvents.OnPayPartiallyClicked -> {
                if (_uiState.value.payPartiallyAmount.toDouble() / 100 >= _uiState.value.debtSelected!!.amount) {
                    markDebtAsPaid(debt = _uiState.value.debtSelected!!)
                } else {
                    payPartially()
                }
            }
        }
    }

    private fun payPartially() {
        _isLoading.update { true }
        debtRepository.makePartialPayment(
            debt = _uiState.value.debtSelected!!,
            amount = _uiState.value.payPartiallyAmount.toDouble() / 100
        ) { response ->
            when (response) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            debtSelected = null,
                            onDebtMarkedAsPaid = true
                        )
                    }
                    _isLoading.update { false }
                    getAllDebts()
                }

                is Resource.Error -> {
                    _errorMessage.update { response.message }
                    _isLoading.update { false }
                }
            }
        }
    }

    private fun markDebtAsPaid(debt: Debt) {
        _isLoading.update { true }
        debtRepository.markDebtAsPaid(debt) { response ->
            when (response) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            debtSelected = null,
                            onDebtMarkedAsPaid = true
                        )
                    }
                    _isLoading.update { false }
                    getAllDebts()
                }

                is Resource.Error -> {
                    _errorMessage.update { response.message }
                    _isLoading.update { false }
                }
            }
        }
    }
}