package com.cequea.elrinconcitodaluz.ui.screens.debts.screens

import android.content.res.Configuration
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cequea.elrinconcitodaluz.R
import com.cequea.elrinconcitodaluz.common.LocalColor
import com.cequea.elrinconcitodaluz.common.LocalSpacing
import com.cequea.elrinconcitodaluz.common.getBalancedFormatted
import com.cequea.elrinconcitodaluz.domain.model.Debt
import com.cequea.elrinconcitodaluz.domain.model.ProductDebt
import com.cequea.elrinconcitodaluz.ui.common.SearchCard
import com.cequea.elrinconcitodaluz.ui.navigation.AppScreens
import com.cequea.elrinconcitodaluz.ui.screens.debts.DebtsEvents
import com.cequea.elrinconcitodaluz.ui.screens.debts.DebtsViewModel
import com.cequea.elrinconcitodaluz.ui.theme.AppTheme
import java.time.LocalDateTime

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DebtsScreen(
    viewModel: DebtsViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val pullRefreshState = rememberPullRefreshState(isLoading, { viewModel.getAllDebts() })


    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        DebtsContent(
            modifier = Modifier,
            queryValue = uiState.searchBarText,
            debts = uiState.debts,
            onEvent = viewModel::onEvent,
            onSeeDetailsClicked = { navController.navigate(AppScreens.DebtDetailScreen.route) },
            onNavigateToSalesScreenClicked = { navController.navigate(AppScreens.AddSaleScreen.route) }
        )

        PullRefreshIndicator(isLoading, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun DebtsContent(
    modifier: Modifier = Modifier,
    queryValue: String,
    debts: List<Debt>,
    onEvent: (DebtsEvents) -> Unit,
    onSeeDetailsClicked: (Debt) -> Unit,
    onNavigateToSalesScreenClicked: () -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))
        SearchCard(
            onValueChange = { onEvent(DebtsEvents.OnQueryChanged(it)) },
            value = queryValue,
            placeholderText = stringResource(id = R.string.search_bar_text)
        )

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        BoxWithConstraints {
            LazyColumn(modifier = Modifier.height(maxHeight)) {
                items(debts.size) { index ->
                    DebtNewItem(
                        debt = debts[index],
                        onSeeDetailsClicked = { debt ->
                            onEvent(DebtsEvents.OnDebtClicked(debt))
                            onSeeDetailsClicked(debt)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DebtNewItem(
    modifier: Modifier = Modifier,
    debt: Debt,
    onSeeDetailsClicked: (Debt) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(0.7f)
        ) {
            Text(
                modifier = Modifier.padding(start = LocalSpacing.current.medium),
                text = "${debt.personName}: ${debt.getListOfProducts()}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W600
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier.padding(start = LocalSpacing.current.medium),
                text = debt.amount.getBalancedFormatted(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W400
                )
            )
        }

        Button(
            modifier = Modifier.weight(0.3f),
            onClick = { onSeeDetailsClicked(debt) }
        ) {
            Text(text = stringResource(id = R.string.see_debt_details))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DebtNewItemPreview() {
    AppTheme {
        DebtNewItem(
            debt = Debt(
                id = "1",
                personName = "Juan",
                products = listOf(
                    ProductDebt(
                        id = "1",
                        name = "Nutella",
                        price = 10.0,
                        quantity = 1,
                        total = 10.0,
                        purchasePrice = 2.00
                    ),
                    ProductDebt(
                        id = "2",
                        name = "Pringles",
                        price = 2.5,
                        quantity = 3,
                        total = 10.0,
                        purchasePrice = 2.00
                    ),
                ),
                date = LocalDateTime.now(),
                isPaid = false,
                amount = 3.0
            ),
            onSeeDetailsClicked = {}
        )
    }
}


@Composable
fun DebtDetail(debt: Debt, onEvent: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = LocalSpacing.current.large,
                end = LocalSpacing.current.large,
                top = LocalSpacing.current.medium
            )
    ) {
        repeat(debt.products.size) { index ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${debt.products[index].quantity} x ${debt.products[index].name}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Total: ${(debt.products[index].price * debt.products[index].quantity).getBalancedFormatted()}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.padding(top = LocalSpacing.current.medium))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LocalSpacing.current.medium),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = stringResource(id = R.string.paid_text))
            }

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalColor.current.expense
                ),
                onClick = { /*TODO*/ }
            ) {
                Text(text = stringResource(id = R.string.add_debt))
            }
        }
    }
}

val debtsPreview = listOf(
    Debt(
        id = "1",
        personName = "Juan",
        products = listOf(
            ProductDebt(
                id = "1",
                name = "Nutella",
                price = 10.0,
                quantity = 1,
                total = 10.0,
                purchasePrice = 2.00
            ),
            ProductDebt(
                id = "2",
                name = "Pringles",
                price = 2.5,
                quantity = 3,
                total = 10.0,
                purchasePrice = 2.00
            ),
        ),
        date = LocalDateTime.now(),
        isPaid = false,
        amount = 3.0
    ),
    Debt(
        id = "2",
        personName = "Mario",
        products = listOf(
            ProductDebt(
                id = "1",
                name = "Nutella",
                price = 10.0,
                quantity = 1,
                total = 10.0,
                purchasePrice = 2.00
            ),
            ProductDebt(
                id = "2",
                name = "Pringles",
                price = 2.5,
                quantity = 3,
                total = 10.0,
                purchasePrice = 2.00
            ),
        ),
        date = LocalDateTime.now(),
        isPaid = false,
        amount = 3.0
    ),
    Debt(
        id = "3",
        personName = "Pedro",
        products = listOf(
            ProductDebt(
                id = "1",
                name = "Nutella",
                price = 10.0,
                quantity = 1,
                total = 10.0,
                purchasePrice = 2.00
            ),
            ProductDebt(
                id = "2",
                name = "Pringles",
                price = 2.5,
                quantity = 3,
                total = 10.0,
                purchasePrice = 2.00
            ),
        ),
        date = LocalDateTime.now(),
        isPaid = false,
        amount = 3.0
    ),
)

@Composable
@Preview(showBackground = true)
fun DebtsContentPreview() {
    AppTheme {
        DebtsContent(
            queryValue = "",
            debts = debtsPreview,
            onEvent = {},
            onSeeDetailsClicked = { }
        ) { }
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun DebtsContentDarkPreview() {
    AppTheme {
        DebtsContent(
            queryValue = "",
            debts = debtsPreview,
            onEvent = {},
            onSeeDetailsClicked = { }
        ) { }
    }
}