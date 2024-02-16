package com.cequea.elrinconcitodaluz.ui.screens.inventory

import androidx.lifecycle.ViewModel
import com.cequea.elrinconcitodaluz.domain.repository.ProductRepository
import com.cequea.elrinconcitodaluz.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<InventoryUIState> = MutableStateFlow(InventoryUIState())
    val uiState: StateFlow<InventoryUIState> = _uiState.asStateFlow()

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

    fun onEvent(event: InventoryEvents) {
        when (event) {
            is InventoryEvents.OnErrorMessageHandled -> {
                _errorMessage.update { null }
            }

            is InventoryEvents.OnSaveProductClicked -> {
                updateProduct()
            }

            is InventoryEvents.OnSaveProductHandled -> {
                _uiState.update { it.copy(productSaved = true) }
            }

            is InventoryEvents.OnProductSelected -> {
                _uiState.update { it.copy(
                    productSelected = event.product,
                    name = event.product.name,
                    category = event.product.category,
                    price = event.product.price.toString(),
                    quantity = event.product.stock.toString(),
                    percentageDistribution = event.product.percentageDistribution

                ) }
            }

            is InventoryEvents.OnPersonChanged -> {
                _uiState.update { it.copy(name = event.string) }
            }

            is InventoryEvents.OnCategoryChanged -> {
                _uiState.update { it.copy(category = event.string) }
            }

            is InventoryEvents.OnPriceChanged -> {
                _uiState.update { it.copy(price = event.string) }
            }

            is InventoryEvents.OnQuantityChanged -> {
                _uiState.update { it.copy(quantity = event.string) }
            }

            is InventoryEvents.OnPercentageDistributionChanged -> {
                val percentageDistribution = _uiState.value.percentageDistribution.toMutableMap()
                percentageDistribution[event.key] = event.value.toDouble()
                _uiState.update { it.copy(percentageDistribution = percentageDistribution) }
            }
        }
    }

    private fun updateProduct(){
        val product = _uiState.value.productSelected!!
        val updatedProduct = product.copy(
            name = _uiState.value.name,
            category = _uiState.value.category,
            price = _uiState.value.price.toDouble(),
            stock = _uiState.value.quantity.toInt(),
            percentageDistribution = _uiState.value.percentageDistribution
        )

        _isLoading.update { true }
        productRepository.updateProduct(updatedProduct) { response ->
            when (response) {
                is Resource.Success -> {
                    _uiState.update { it.copy(productSaved = true) }
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