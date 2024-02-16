package com.cequea.elrinconcitodaluz

import androidx.lifecycle.ViewModel
import com.cequea.elrinconcitodaluz.domain.data.ProductConfigurations
import com.cequea.elrinconcitodaluz.domain.repository.ProductRepository
import com.cequea.elrinconcitodaluz.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel() {

    private val _uiState: MutableStateFlow<MainUIState> = MutableStateFlow(
        MainUIState()
    )
    val uiState: StateFlow<MainUIState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(isLoading = true) }
        productRepository.getAllProductsConfigurations { response ->
            when (response) {
                is Resource.Success -> {
                    ProductConfigurations.config = response.data!!
                    _uiState.update { it.copy(isLoading = false) }
                }

                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = response.message) }
                }
            }
        }
    }

    fun setTopBarTitle(title: String) {
        _uiState.update { _uiState.value.copy(topBarTittle = title) }
    }
}