package com.cequea.elrinconcitodaluz

data class MainUIState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val topBarTittle: String = "",
)
