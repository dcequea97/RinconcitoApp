package com.cequea.elrinconcitodaluz.ui.screens.debts.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cequea.elrinconcitodaluz.common.CurrencyAmountVisualTransformation
import com.cequea.elrinconcitodaluz.common.LocalSpacing
import com.cequea.elrinconcitodaluz.common.getBalancedFormatted
import com.cequea.elrinconcitodaluz.common.getSimpleFormattedDate
import com.cequea.elrinconcitodaluz.domain.model.Debt
import com.cequea.elrinconcitodaluz.domain.model.ProductDebt
import com.cequea.elrinconcitodaluz.ui.common.InputForm
import com.cequea.elrinconcitodaluz.ui.screens.debts.DebtsEvents
import com.cequea.elrinconcitodaluz.ui.screens.debts.DebtsViewModel
import com.cequea.elrinconcitodaluz.ui.theme.AppTheme
import java.time.LocalDateTime

@Composable
fun DebtDetailScreen(
    navController: NavController,
    viewModel: DebtsViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = uiState.onDebtMarkedAsPaid) {
        if (uiState.onDebtMarkedAsPaid) {
            viewModel.onEvent(DebtsEvents.OnDebtMarkedAsPaidHandled)
            navController.popBackStack()
        }
    }

    uiState.debtSelected?.let { debt ->
        DebtDetailContent(
            debt = debt,
            payPartiallyText = uiState.payPartiallyAmount,
            payPartiallyStatus = uiState.payPartiallyShow,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun DebtDetailContent(
    debt: Debt,
    payPartiallyText: String,
    payPartiallyStatus: Boolean,
    onEvent: (DebtsEvents) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(
                horizontal = LocalSpacing.current.medium,
                vertical = LocalSpacing.current.medium
            )
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoItem(
                title = debt.personName,
                value = debt.products.sumOf { it.total }.getBalancedFormatted(),
            )

            InfoItem(
                title = "Fecha",
                value = debt.date.getSimpleFormattedDate()
            )
        }


        Divider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f)
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            InfoItem(
                modifier = Modifier.weight(0.5f),
                title = "Monto Pagado",
                value = (debt.products.sumOf { it.total } - debt.amount).getBalancedFormatted()
            )

            InfoItem(
                modifier = Modifier.weight(0.5f),
                title = "Monto Pendiente",
                value = debt.amount.getBalancedFormatted()
            )

        }

        Divider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.20f)
        )

        Text(
            modifier = Modifier,
            text = "Productos",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.W700)
        )

        Spacer(modifier = Modifier.padding(bottom = LocalSpacing.current.medium))

        debt.products.forEach { product ->
            ProductItem(product)
        }

        Spacer(modifier = Modifier.padding(bottom = LocalSpacing.current.medium))

        Button(
            modifier = Modifier
                .align(CenterHorizontally)
                .fillMaxWidth(0.8f),
            onClick = { onEvent(DebtsEvents.OnMarkAsPaidClicked(debt)) }
        ) {
            Text(text = "Marcar como pagada")
        }

        FilledTonalButton(
            modifier = Modifier
                .align(CenterHorizontally)
                .fillMaxWidth(0.8f),
            onClick = { onEvent(DebtsEvents.OnPayPartiallyShowClicked) }
        ) {
            Text(text = "Pagar Parcialmente")
        }

        AnimatedVisibility(
            visible = payPartiallyStatus,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                InputForm(
                    text = payPartiallyText,
                    onValueChange = {
                        onEvent(
                            DebtsEvents.OnPayPartiallyAmountChanged(
                                it.filter { char -> char.isDigit() })
                        )
                    },
                    title = "Monto",
                    placeholder = "",
                    focusManager = LocalFocusManager.current,
                    icon = Icons.Outlined.AttachMoney,
                    visualTransformation = CurrencyAmountVisualTransformation(),
                )

                FilledTonalButton(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(top = LocalSpacing.current.small),
                    onClick = {
                        onEvent(DebtsEvents.OnPayPartiallyClicked)
                    }
                ) {
                    Text(text = "Pagar")
                }
            }
        }
    }
}

@Composable
private fun InfoItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Column(
        modifier = modifier
            .padding(bottom = LocalSpacing.current.medium)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ProductItem(
    product: ProductDebt
) {
    Column(
        modifier = Modifier
            .padding(bottom = LocalSpacing.current.medium)
            .fillMaxWidth()
    ) {
        Text(
            text = "${product.quantity}x ${product.name}",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500)
        )

        Text(
            text = product.total.getBalancedFormatted(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
@Preview(showBackground = true)
private fun DebtDetailScreenPreview() {
    AppTheme {
        DebtDetailContent(
            debt = Debt(
                id = "1",
                personName = "Luis",
                amount = 5.0,
                date = LocalDateTime.now(),
                products = listOf(
                    ProductDebt(
                        id = "1",
                        name = "Pringles Grandes",
                        price = 2.50,
                        quantity = 3,
                        total = 7.5,
                        purchasePrice = 2.00
                    ),
                    ProductDebt(
                        id = "4",
                        name = "Milka",
                        price = 2.00,
                        quantity = 2,
                        total = 4.0,
                        purchasePrice = 2.00
                    )
                ),
                isPaid = false
            ),
            onEvent = {},
            payPartiallyText = "",
            payPartiallyStatus = false
        )
    }
}