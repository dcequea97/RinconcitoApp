package com.cequea.elrinconcitodaluz.ui.navigation

sealed class AppScreens(val route: String) {
    data object HomeScreen : AppScreens("home_screen")
    data object DebtsScreen : AppScreens("debts_screen")
    data object DebtDetailScreen : AppScreens("debt_detail_screen")
    data object AddSaleScreen : AppScreens("add_sale_select_products_screen")
    data object SalesHistoryScreen : AppScreens("sales_history_screen")
    data object SalesDetailScreen : AppScreens("sales_detail_screen")
    data object InventoryScreen : AppScreens("inventory_screen")
    data object InventoryProductDetailScreen : AppScreens("inventory_product_detail_screen")
}