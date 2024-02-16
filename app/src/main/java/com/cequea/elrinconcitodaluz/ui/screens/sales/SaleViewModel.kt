package com.cequea.elrinconcitodaluz.ui.screens.sales

import androidx.lifecycle.ViewModel
import com.cequea.elrinconcitodaluz.domain.model.PaymentStatus
import com.cequea.elrinconcitodaluz.domain.repository.ProductRepository
import com.cequea.elrinconcitodaluz.domain.repository.SaleRepository
import com.cequea.elrinconcitodaluz.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SaleViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val saleRepository: SaleRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<SaleUIState> = MutableStateFlow(SaleUIState())
    val uiState: StateFlow<SaleUIState> = _uiState.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        _isLoading.update { true }
        productRepository.getAllProducts { response ->
            when (response) {
                is Resource.Success -> {
                    _uiState.update { it.copy(products = response.data!!) }
                    _isLoading.update { false }
                }

                is Resource.Error -> {
                    _errorMessage.update { response.message }
                    _isLoading.update { false }
                }
            }
        }
    }

    fun onEvent(event: SaleEvents) {
        when (event) {
            is SaleEvents.OnProductClicked -> {
                val productsTemp = _uiState.value.products.toMutableList()
                val index = productsTemp.indexOfFirst { it.id == event.product.id }

                if (index != -1) {
                    productsTemp[index] = event.product
                }

                _uiState.update { uiState ->
                    uiState.copy(products = productsTemp)
                }
            }

            is SaleEvents.OnPersonNameChanged -> {
                _uiState.update { it.copy(personName = event.name) }
            }

            is SaleEvents.OnPaymentStatusChanged -> {
                _uiState.update { it.copy(paymentStatusSelected = event.status) }
            }

            is SaleEvents.OnRegisterSaleClicked -> {
                val products = _uiState.value.products.filter { it.cartQuantity > 0 }
                val personName = _uiState.value.personName
                val paymentStatus = _uiState.value.paymentStatusSelected.id

                if (products.isEmpty()) {
                    _errorMessage.update { "No hay productos en el carrito" }
                    return
                }

                if (personName.isEmpty()) {
                    _errorMessage.update { "El nombre no puede estar vacío" }
                    return
                }

                _isLoading.update { true }
                saleRepository.registerSale(personName, products, paymentStatus) { response ->
                    when (response) {
                        is Resource.Success -> {
                            _isLoading.update { false }
                            _errorMessage.update { null }
                            _uiState.update { it.copy(
                                personName = "",
                                paymentStatusSelected = PaymentStatus(1, "Pendiente"),
                                products = _uiState.value.products.map { product -> product.copy(cartQuantity = 0) },
                                saleRegistered = true
                            ) }
                        }

                        is Resource.Error -> {
                            _isLoading.update { false }
                            _errorMessage.update { response.message }
                        }
                    }
                }
            }

            is SaleEvents.OnErrorMessageHandled -> {
                _errorMessage.update { null }
            }

            is SaleEvents.OnSaleRegisteredHandled -> {
                _uiState.update { it.copy(
                    personName = "",
                    paymentStatusSelected = PaymentStatus(1, "Pendiente"),
                    products = _uiState.value.products.map { product -> product.copy(cartQuantity = 0) },
                    saleRegistered = false
                ) }
            }
        }
    }
}