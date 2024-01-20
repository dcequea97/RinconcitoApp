package com.cequea.elrinconcitodaluz

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val _uiState: MutableStateFlow<MainUIState> = MutableStateFlow(
        MainUIState()
    )
    val uiState: StateFlow<MainUIState> = _uiState.asStateFlow()

}