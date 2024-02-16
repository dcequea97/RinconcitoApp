package com.cequea.elrinconcitodaluz.ui.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cequea.elrinconcitodaluz.MainViewModel
import com.cequea.elrinconcitodaluz.R
import com.cequea.elrinconcitodaluz.ui.common.AppTopBar
import com.cequea.elrinconcitodaluz.ui.common.BottomNavigationItem
import com.cequea.elrinconcitodaluz.ui.screens.debts.DebtsViewModel
import com.cequea.elrinconcitodaluz.ui.screens.debts.screens.DebtDetailScreen
import com.cequea.elrinconcitodaluz.ui.screens.debts.screens.DebtsScreen
import com.cequea.elrinconcitodaluz.ui.screens.home.HomeScreen
import com.cequea.elrinconcitodaluz.ui.screens.inventory.InventoryProductDetailScreen
import com.cequea.elrinconcitodaluz.ui.screens.inventory.InventoryScreen
import com.cequea.elrinconcitodaluz.ui.screens.inventory.InventoryViewModel
import com.cequea.elrinconcitodaluz.ui.screens.sales.salesHistory.SaleDetailScreen
import com.cequea.elrinconcitodaluz.ui.screens.sales.salesHistory.SalesHistoryScreen
import com.cequea.elrinconcitodaluz.ui.screens.sales.salesHistory.SalesHistoryViewModel
import com.cequea.elrinconcitodaluz.ui.screens.sales.screens.AddSaleScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()

    var navigationSelectedItem by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                tittle = uiState.topBarTittle,
                onBackClicked = { navController.popBackStack() }
            )
        },
        bottomBar = {
            NavigationBar {
                //getting the list of bottom navigation items for our data class
                BottomNavigationItem().bottomNavigationItems().forEachIndexed { index, navigationItem ->

                    //iterating all items with their respective indexes
                    NavigationBarItem(
                        selected = index == navigationSelectedItem,
                        label = {
                            Text(navigationItem.label)
                        },
                        icon = {
                            Icon(
                                navigationItem.icon,
                                contentDescription = navigationItem.label
                            )
                        },
                        onClick = {
                            navigationSelectedItem = index
                            navController.navigate(navigationItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        BoxWithConstraints(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = AppScreens.HomeScreen.route
            ) {
                composable(AppScreens.HomeScreen.route) {
                    viewModel.setTopBarTitle(stringResource(id = R.string.home_tittle))
                    HomeScreen(navController = navController)
                }


                composable(AppScreens.DebtsScreen.route) {
                    viewModel.setTopBarTitle(stringResource(id = R.string.debts_tittle))
                    DebtsScreen(navController = navController)
                }

                composable(AppScreens.DebtDetailScreen.route) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(AppScreens.DebtsScreen.route)
                    }
                    val debtsViewModel = hiltViewModel<DebtsViewModel>(parentEntry)
                    viewModel.setTopBarTitle(stringResource(id = R.string.debts_detail_tittle))

                    DebtDetailScreen(navController = navController, viewModel = debtsViewModel)
                }

                composable(AppScreens.AddSaleScreen.route) {
                    viewModel.setTopBarTitle(stringResource(id = R.string.register_sale_tittle))
                    AddSaleScreen(navController = navController)
                }

                composable(AppScreens.SalesHistoryScreen.route) {
                    viewModel.setTopBarTitle(stringResource(id = R.string.sales_history_tittle))
                    SalesHistoryScreen(navController = navController)
                }

                composable(AppScreens.SalesDetailScreen.route) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(AppScreens.SalesHistoryScreen.route)
                    }
                    val salesViewModel = hiltViewModel<SalesHistoryViewModel>(parentEntry)
                    viewModel.setTopBarTitle(stringResource(id = R.string.sale_detail_tittle))
                    SaleDetailScreen(navController = navController, viewModel = salesViewModel)
                }

                composable(AppScreens.InventoryScreen.route){
                    viewModel.setTopBarTitle(stringResource(id = R.string.inventory_tittle))
                    InventoryScreen(navController = navController)
                }

                composable(AppScreens.InventoryProductDetailScreen.route){ backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(AppScreens.InventoryScreen.route)
                    }
                    val inventoryViewModel = hiltViewModel<InventoryViewModel>(parentEntry)
                    viewModel.setTopBarTitle(stringResource(id = R.string.product_detail_tittle))
                    InventoryProductDetailScreen(viewModel = inventoryViewModel)
                }
            }
        }
    }
}