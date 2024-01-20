package com.cequea.elrinconcitodaluz.ui.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cequea.elrinconcitodaluz.MainViewModel
import com.cequea.elrinconcitodaluz.ui.screens.debts.DebtsScreen
import com.cequea.elrinconcitodaluz.ui.screens.sales.AddSaleScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val navController =  rememberNavController()

    Scaffold { innerPadding ->
        BoxWithConstraints(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = AppScreens.DebtsScreen.route
            ){
                composable(AppScreens.DebtsScreen.route){
                    DebtsScreen(navController = navController)
                }

                composable(AppScreens.AddSaleScreen.route){
                    AddSaleScreen(navController = navController)
                }
            }
        }
    }
}