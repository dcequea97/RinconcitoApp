package com.cequea.elrinconcitodaluz.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.cequea.elrinconcitodaluz.ui.theme.AppTheme
import java.time.LocalDate
import java.time.Month

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullRefreshState = rememberPullRefreshState(uiState.isLoading, { viewModel.getSalesByMonthSelected() })

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeContent(
            modifier = Modifier
                .height(maxHeight)
                .fillMaxWidth()
                .pullRefresh(pullRefreshState),
            uiState = uiState,
            onEvent = viewModel::onEvent
        )

        PullRefreshIndicator(uiState.isLoading, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}


@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    uiState: HomeUIState,
    onEvent: (HomeEvents) -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MonthOptions(
            months = uiState.months,
            onMonthSelected = { onEvent(HomeEvents.OnMonthSelected(it)) },
            monthSelected = uiState.monthSelected
        )

        Text(
            modifier = Modifier.padding(vertical = LocalSpacing.current.medium),
            text = "Primera Quincena",
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LocalSpacing.current.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HomeCardItem(
                modifier = Modifier.weight(0.5f),
                tittle = "Total de ventas",
                value = uiState.totalSalesFirstQ,
                onClick = {}
            )
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LocalSpacing.current.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HomeCardItem(
                modifier = Modifier.weight(0.5f),
                tittle = "Total de ingresos",
                value = uiState.totalIncomesFirstQ,
                onClick = {}
            )

            Spacer(modifier = Modifier.width(LocalSpacing.current.small))

            HomeCardItem(
                modifier = Modifier.weight(0.5f),
                tittle = "Total de gastos",
                value = uiState.totalExpensesFirstQ,
                onClick = {}
            )
        }

        ResumeByMonthCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.medium),
            totalSaleByProduct = uiState.salesByProductFirstQ
        )

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        Text(
            modifier = Modifier.padding(vertical = LocalSpacing.current.medium),
            text = "Segunda Quincena",
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LocalSpacing.current.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HomeCardItem(
                modifier = Modifier.weight(0.5f),
                tittle = "Total de ventas",
                value = uiState.totalSalesSecondQ,
                onClick = {}
            )
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LocalSpacing.current.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HomeCardItem(
                modifier = Modifier.weight(0.5f),
                tittle = "Total de ingresos",
                value = uiState.totalIncomesSecondQ,
                onClick = {}
            )

            Spacer(modifier = Modifier.width(LocalSpacing.current.small))

            HomeCardItem(
                modifier = Modifier.weight(0.5f),
                tittle = "Total de gastos",
                value = uiState.totalExpensesSecondQ,
                onClick = {}
            )
        }

        ResumeByMonthCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.medium),
            totalSaleByProduct = uiState.salesByProductSecondQ
        )

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))
    }
}

@Composable
fun ResumeByMonthCard(
    modifier: Modifier = Modifier,
    totalSaleByProduct: Map<String, Double>,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(LocalSpacing.current.small),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        )
    ) {
        Column(
            modifier = Modifier
                .padding(LocalSpacing.current.medium),
            verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.small),
            horizontalAlignment = Alignment.Start
        ) {
            totalSaleByProduct.forEach { (configurationName, totalSale) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = configurationName,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal)
                    )

                    Text(
                        text = totalSale.getBalancedFormatted(),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeCardItem(
    modifier: Modifier = Modifier,
    tittle: String,
    value: Double,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(74.dp),
        shape = RoundedCornerShape(LocalSpacing.current.small)
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = value.getBalancedFormatted(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = tittle,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light
                    )
                )
            }
        }
    }
}

@Composable
private fun MonthOptions(
    modifier: Modifier = Modifier,
    months: List<LocalDate>,
    onMonthSelected: (LocalDate) -> Unit,
    monthSelected: LocalDate?
) {
    LazyRow(modifier = modifier) {
        item {
            Spacer(modifier = Modifier.padding(start = LocalSpacing.current.small))
        }

        items(months.size) { index ->
            Box(
                modifier = Modifier
                    .background(
                        if (months[index] == monthSelected) MaterialTheme.colorScheme.tertiaryContainer
                        else LocalColor.current.grayBackground,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(LocalSpacing.current.small)
                    .clickable { onMonthSelected(months[index]) }
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    months[index].month?.name?.replaceFirstChar(Char::uppercase)
                        ?.let { Text(text = it) }
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
@Preview(showBackground = true)
fun HomeCardItemPreview() {
    AppTheme {
        HomeCardItem(
            tittle = "Total de ventas",
            value = 100.00,
            onClick = {}
        )
    }
}


@Composable
@Preview(showBackground = true)
private fun HomeContentPreview() {
    AppTheme {
        HomeContent(
            uiState = HomeUIState(
                months = listOf(
                    LocalDate.now(),
                    LocalDate.now().plusMonths(1),
                    LocalDate.now().plusMonths(2),
                    LocalDate.now().plusMonths(3),
                    LocalDate.now().plusMonths(4),
                    LocalDate.now().plusMonths(5),
                    LocalDate.now().plusMonths(6),
                    LocalDate.now().plusMonths(7),
                    LocalDate.now().plusMonths(8),
                    LocalDate.now().plusMonths(9),
                    LocalDate.now().plusMonths(10),
                    LocalDate.now().plusMonths(11),
                ),
                monthSelected = LocalDate.now()
            ),
            onEvent = {}
        )
    }
}
