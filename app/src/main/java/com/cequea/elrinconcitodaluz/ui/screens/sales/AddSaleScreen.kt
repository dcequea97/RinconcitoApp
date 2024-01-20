package com.cequea.elrinconcitodaluz.ui.screens.sales

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cequea.elrinconcitodaluz.domain.model.Product
import kotlin.reflect.KFunction1

@Composable
fun AddSaleScreen(navController: NavHostController, viewModel: SaleViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsState()

    AddSaleContent(
        onEvent = viewModel::onEvent,
        personName = uiState.personName,
        quantity = uiState.quantity,
        total = uiState.total,
        products = uiState.products,
        productSelected = uiState.productSelected,
    )

}

@Composable
fun AddSaleContent(
    onEvent: (SaleEvents) -> Unit,
    personName: String,
    quantity: String,
    total: String,
    products: List<Product>,
    productSelected: Product?
) {
    TODO("Not yet implemented")
}

@Composable
@Preview(showBackground = true)
fun AddSaleContentPreview(){
    MaterialTheme{
        AddSaleContent(
            onEvent = {},
            personName = "David",
            quantity = "1",
            total = "28,50",
            products = emptyList(),
            productSelected = null
        )
    }
}
