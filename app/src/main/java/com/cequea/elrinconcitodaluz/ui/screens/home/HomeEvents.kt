package com.cequea.elrinconcitodaluz.ui.screens.home

import java.time.LocalDate
import java.time.Month

sealed class HomeEvents{
    data class OnMonthSelected(val month: LocalDate) : HomeEvents()
}
