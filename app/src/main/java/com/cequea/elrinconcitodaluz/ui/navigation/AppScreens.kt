package com.cequea.elrinconcitodaluz.ui.navigation

sealed class AppScreens(val route: String) {
    data object MainScreen : AppScreens("main_screen")
    data object DebtsScreen : AppScreens("debts_screen")
    data object AddSaleScreen : AppScreens("add_sale_screen")
}