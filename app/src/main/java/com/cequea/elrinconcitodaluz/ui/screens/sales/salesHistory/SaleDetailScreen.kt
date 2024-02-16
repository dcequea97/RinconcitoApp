package com.cequea.elrinconcitodaluz.ui.screens.sales.salesHistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cequea.elrinconcitodaluz.common.LocalSpacing
import com.cequea.elrinconcitodaluz.common.getBalancedFormatted
import com.cequea.elrinconcitodaluz.common.getSimpleFormattedDate
import com.cequea.elrinconcitodaluz.domain.model.ProductDebt
import com.cequea.elrinconcitodaluz.domain.model.Sale
import com.cequea.elrinconcitodaluz.ui.theme.AppTheme
import java.time.LocalDateTime

@Composable
fun SaleDetailScreen(navController: NavHostController, viewModel: SalesHistoryViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    uiState.saleSelected?.let { sale ->
        SaleDetailContent(
            sale = sale
        )
    }
}


@Composable
fun SaleDetailContent(
    sale: Sale
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
                title = sale.personName,
                value = sale.total.getBalancedFormatted(),
            )

            InfoItem(
                title = "Fecha",
                value = sale.date.getSimpleFormattedDate()
            )
        }


        Divider(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f)
        )

        Text(
            modifier = Modifier,
            text = "Productos",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.W700)
        )

        Spacer(modifier = Modifier.padding(bottom = LocalSpacing.current.medium))

        sale.products.forEach { product ->
            ProductItem(product)
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
private fun SaleDetailScreenPreview() {
    AppTheme {
        SaleDetailContent(
            sale = Sale(
                id = "1",
                personName = "Luis",
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
                total = 10.5,
            )
        )
    }
}