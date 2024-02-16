package com.cequea.elrinconcitodaluz.ui.screens.expenses

import android.widget.Toast
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cequea.elrinconcitodaluz.common.CurrencyAmountVisualTransformation
import com.cequea.elrinconcitodaluz.common.LocalSpacing
import com.cequea.elrinconcitodaluz.ui.common.InputForm
import com.cequea.elrinconcitodaluz.ui.common.LoadingProgressBar
import com.cequea.elrinconcitodaluz.ui.screens.debts.DebtsEvents
import com.cequea.elrinconcitodaluz.ui.screens.home.HomeUIState
import com.cequea.elrinconcitodaluz.ui.screens.sales.SaleEvents
import com.cequea.elrinconcitodaluz.ui.theme.AppTheme

@Composable
fun ExpensesScreen(
    navController: NavController,
    viewModel: ExpensesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.errorMessage) {
        uiState.errorMessage?.let {
            viewModel.onEvent(ExpensesEvents.OnErrorMessageHandled)
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = uiState.expenseRegistered) {
        if (uiState.expenseRegistered) {
            viewModel.onEvent(ExpensesEvents.OnExpenseRegisteredHandled)
            navController.popBackStack()
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        ExpensesContent(
            amount = uiState.amount,
            description = uiState.description,
            onEvent = viewModel::onEvent
        )
        if (uiState.isLoading) {
            LoadingProgressBar(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun ExpensesContent(
    amount: String,
    description: String,
    onEvent: (ExpensesEvents) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxSize().padding(
            horizontal = LocalSpacing.current.medium,
            vertical = LocalSpacing.current.medium
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InputForm(
            text = description,
            onValueChange = { onEvent(ExpensesEvents.OnDescriptionChanged(it)) },
            title = "Descripcion:",
            placeholder = "Ingresar",
            focusManager = focusManager,
            icon = Icons.Outlined.Description
        )

        InputForm(
            text = amount,
            onValueChange = {
                onEvent(
                    ExpensesEvents.OnAmountChanged(
                        it.filter { char -> char.isDigit() })
                )
            },
            title = "Monto",
            placeholder = "",
            focusManager = LocalFocusManager.current,
            icon = Icons.Outlined.AttachMoney,
            visualTransformation = CurrencyAmountVisualTransformation(),
        )

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        Button(
            modifier = Modifier
                .fillMaxWidth(0.8f),
            onClick = { onEvent(ExpensesEvents.OnAddExpenseClicked) }
        ) {
            Text(text = "Registrar Gasto")
        }
    }



}

@Composable
@Preview(showBackground = true)
fun ExpensesScreenPreview() {
    AppTheme {
        ExpensesContent(
            amount = "100",
            description = "Test",
            onEvent = {}
        )
    }
}
