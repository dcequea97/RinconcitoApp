package com.cequea.elrinconcitodaluz.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.ViewTimeline
import androidx.compose.ui.graphics.vector.ImageVector
import com.cequea.elrinconcitodaluz.ui.navigation.AppScreens

//initializing the data class with default parameters
data class BottomNavigationItem(
    val label : String = "",
    val icon : ImageVector = Icons.Filled.Home,
    val route : String = ""
) {
    //function to get the list of bottomNavigationItems
    fun bottomNavigationItems() : List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Inicio",
                icon = Icons.Filled.Home,
                route = AppScreens.HomeScreen.route
            ),
            BottomNavigationItem(
                label = "Deudas",
                icon = Icons.Filled.AttachMoney,
                route = AppScreens.DebtsScreen.route
            ),
            BottomNavigationItem(
                label = "Registrar",
                icon = Icons.Filled.AddBox,
                route = AppScreens.AddSaleScreen.route
            ),
            BottomNavigationItem(
                label = "Historico",
                icon = Icons.Filled.History,
                route = AppScreens.SalesHistoryScreen.route
            ),
            BottomNavigationItem(
                label = "Inventario",
                icon = Icons.Filled.Inventory,
                route = AppScreens.InventoryScreen.route
            )
        )
    }
}