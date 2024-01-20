package com.cequea.elrinconcitodaluz.ui.screens.debts

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.cequea.elrinconcitodaluz.ui.theme.AppTheme
import java.time.LocalDateTime

@Composable
fun DebtsScreen(
    viewModel: DebtsViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    DebtsContent(
        modifier = Modifier,
        queryValue = uiState.searchBarText,
        debts = uiState.debts,
        onEvent = viewModel::onEvent,
        onNavigateToSalesScreenClicked = { navController.navigate(AppScreens.AddSaleScreen.route) }
    )
}

@Composable
fun DebtsContent(
    modifier: Modifier = Modifier,
    queryValue: String,
    debts: List<Debt>,
    onEvent: (DebtsEvents) -> Unit,
    onNavigateToSalesScreenClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()

    ) {
        Column {
            SearchCard(
                onValueChange = { onEvent(DebtsEvents.OnQueryChanged(it)) },
                value = queryValue,
                placeholderText = stringResource(id = R.string.search_bar_text)
            )

            LazyColumn {
                items(debts.size) { index ->
                    DebtItem(
                        debt = debts[index],
                        onEvent = { onEvent(DebtsEvents.OnDebtClicked(it)) }
                    )
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    bottom = LocalSpacing.current.large,
                    end = LocalSpacing.current.large
                ),
            onClick = onNavigateToSalesScreenClicked
        ) {
            Icon(Icons.Filled.Add, "Add")
        }
    }
}

@Composable
fun DebtItem(debt: Debt, onEvent: (String) -> Unit) {
    var showDetail = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                top = LocalSpacing.current.medium
            )
            .clickable { showDetail.value = !showDetail.value }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = LocalSpacing.current.large,
                    end = LocalSpacing.current.large,
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = debt.personName.take(1)
                    )
                }

                Column {
                    Text(
                        modifier = Modifier.padding(start = LocalSpacing.current.medium),
                        text = debt.personName,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        modifier = Modifier.padding(start = LocalSpacing.current.medium),
                        text = debt.getListOfProducts(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Text(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically),
                text = debt.getTotalAmount().getBalancedFormatted(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = LocalColor.current.income
                )
            )
        }

        AnimatedVisibility(
            modifier = Modifier.padding(horizontal = LocalSpacing.current.large),
            visible = showDetail.value,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            DebtDetail(
                debt = debt,
                onEvent = onEvent
            )
        }

        Divider(
            modifier = Modifier.padding(top = LocalSpacing.current.medium),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
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

@Composable
@Preview(showBackground = true)
fun DebtItemPreview() {
    AppTheme {
        DebtDetail(
            debt = Debt(
                id = "1",
                personName = "Juan",
                products = listOf(
                    ProductDebt(
                        id = "1",
                        name = "Nutella",
                        price = 10.0,
                        quantity = 1
                    ),
                    ProductDebt(
                        id = "2",
                        name = "Pringles",
                        price = 2.5,
                        quantity = 3
                    ),
                ),
                date = LocalDateTime.now(),
                isPaid = false,
                amount = 3.0
            ),
            onEvent = {}
        )
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
                quantity = 1
            ),
            ProductDebt(
                id = "2",
                name = "Pringles",
                price = 2.5,
                quantity = 3
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
                quantity = 1
            ),
            ProductDebt(
                id = "2",
                name = "Pringles",
                price = 2.5,
                quantity = 3
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
                quantity = 1
            ),
            ProductDebt(
                id = "2",
                name = "Pringles",
                price = 2.5,
                quantity = 3
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
            onEvent = {}
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
            onEvent = {}
        ) {  }
    }
}