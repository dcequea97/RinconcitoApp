package com.cequea.elrinconcitodaluz.ui.screens.sales.salesHistory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cequea.elrinconcitodaluz.common.LocalColor
import com.cequea.elrinconcitodaluz.common.LocalSpacing
import com.cequea.elrinconcitodaluz.common.getBalancedFormatted
import com.cequea.elrinconcitodaluz.common.getDateWithMonthName
import com.cequea.elrinconcitodaluz.domain.model.DateOptionSelected
import com.cequea.elrinconcitodaluz.domain.model.ProductDebt
import com.cequea.elrinconcitodaluz.domain.model.Sale
import com.cequea.elrinconcitodaluz.ui.common.AppIconButton
import com.cequea.elrinconcitodaluz.ui.common.LoadingProgressBar
import com.cequea.elrinconcitodaluz.ui.navigation.AppScreens
import com.cequea.elrinconcitodaluz.ui.theme.AppTheme
import java.time.LocalDateTime

@Composable
fun SalesHistoryScreen(
    navController: NavController,
    viewModel: SalesHistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        SalesHistoryContent(
            modifier = Modifier.height(maxHeight),
            sales = uiState.sales,
            onEvent = viewModel::onEvent,
            dateOptions = uiState.dateOptions,
            dateOptionSelected = uiState.dateOptionSelected,
            onSaleClicked = {
                navController.navigate(AppScreens.SalesDetailScreen.route)
            }
        )

        if (isLoading){
            LoadingProgressBar(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun SalesHistoryContent(
    modifier: Modifier = Modifier,
    sales: List<Sale>,
    dateOptions: List<DateOptionSelected>,
    dateOptionSelected: DateOptionSelected?,
    onEvent: (SaleHistoryEvents) -> Unit,
    onSaleClicked: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        DateOptions(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = LocalSpacing.current.medium,
                    bottom = LocalSpacing.current.medium
                ),
            onDateSelected = { dateOptionSelected ->
                onEvent(SaleHistoryEvents.OnDateSelected(dateOptionSelected))
            },
            dateOptions = dateOptions,
            dateOptionSelected = dateOptionSelected
        )
        Column(
            modifier = Modifier.padding(horizontal = LocalSpacing.current.medium)
        ) {
            Text(
                modifier = Modifier,
                text = "Ventas",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.W700)
            )

            LazyColumn {
                items(sales.size) { index ->
                    SalesHistoryItem(
                        sale = sales[index],
                        onSaleSelected = { sale ->
                            onEvent(SaleHistoryEvents.OnSaleSelected(sale))
                            onSaleClicked()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DateOptions(
    modifier: Modifier = Modifier,
    dateOptions: List<DateOptionSelected>,
    onDateSelected: (DateOptionSelected) -> Unit,
    dateOptionSelected: DateOptionSelected?
) {
    LazyRow(modifier = modifier) {
        item {
            Spacer(modifier = Modifier.padding(start = LocalSpacing.current.small))
        }

        items(dateOptions.size) { index ->
            Box(
                modifier = Modifier
                    .background(
                        if (dateOptions[index] == dateOptionSelected) MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.4f
                        )
                        else LocalColor.current.grayBackground,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(LocalSpacing.current.small)
                    .clickable { onDateSelected(dateOptions[index]) }
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = dateOptions[index].name)
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Arrow Drop Down"
                    )
                }
            }
            Spacer(modifier = Modifier.padding(end = LocalSpacing.current.small))
        }
    }
}

@Composable
fun SalesHistoryItem(
    sale: Sale,
    onSaleSelected: (Sale) -> Unit
) {
    Row {
        Row(
            modifier = Modifier
                .padding(LocalSpacing.current.medium)
                .fillMaxWidth()
                .weight(0.85f)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.7f)
            ) {
                Text(
                    text = sale.personName,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = sale.date.getDateWithMonthName(),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W500)
                )
            }

            Text(
                modifier = Modifier
                    .weight(0.3f)
                    .align(Alignment.CenterVertically),
                text = sale.total.getBalancedFormatted(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.W500,
                    textAlign = TextAlign.End
                )
            )
        }

        AppIconButton(
            modifier = Modifier
                .weight(0.15f)
                .align(Alignment.CenterVertically),
            onClick = { onSaleSelected(sale) },
            icon = Icons.Outlined.ArrowForward,
            contentDescription = "Detalle"
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SalesHistoryScreenPreview() {
    AppTheme {
        SalesHistoryContent(
            sales = listOf(
                Sale(
                    id = "1",
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
                    personName = "Luis",
                    total = 5.0,
                    date = LocalDateTime.now()
                ),
                Sale(
                    id = "2",
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
                    personName = "Luis",
                    total = 5.0,
                    date = LocalDateTime.now()
                ),
                Sale(
                    id = "3",
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
                    personName = "Luis",
                    total = 5.0,
                    date = LocalDateTime.now(
                    )
                )
            ),
            onEvent = {},
            dateOptions = listOf(
                DateOptionSelected(0, "Todas"),
                DateOptionSelected(1, "Hoy"),
                DateOptionSelected(2, "Ayer"),
                DateOptionSelected(3, "Esta semana"),
                DateOptionSelected(4, "Esta quincena"),
                DateOptionSelected(5, "Este Mes"),
            ),
            dateOptionSelected = DateOptionSelected(0, "Todas"),
            onSaleClicked = {}
        )
    }
}