package com.cequea.elrinconcitodaluz.ui.screens.inventory

import android.widget.Toast
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cequea.elrinconcitodaluz.common.LocalSpacing
import com.cequea.elrinconcitodaluz.common.getBalancedFormatted
import com.cequea.elrinconcitodaluz.domain.model.Product
import com.cequea.elrinconcitodaluz.ui.common.AppIconButton
import com.cequea.elrinconcitodaluz.ui.common.LoadingProgressBar
import com.cequea.elrinconcitodaluz.ui.navigation.AppScreens

@Composable
fun InventoryScreen(
    navController: NavHostController,
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(key1 = errorMessage){
        errorMessage?.let {
            viewModel.onEvent(InventoryEvents.OnErrorMessageHandled)
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = uiState.productSaved){
        if (uiState.productSaved){
            viewModel.onEvent(InventoryEvents.OnSaveProductHandled)
            navController.popBackStack()
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        InventoryContent(
            onProductSelected = { product ->
                viewModel.onEvent(InventoryEvents.OnProductSelected(product))
                navController.navigate(AppScreens.InventoryProductDetailScreen.route)
            },
            products = uiState.products
        )

        if (isLoading) {
            LoadingProgressBar(modifier = Modifier.align(Alignment.Center))
        }
    }

}

@Composable
private fun InventoryContent(
    onProductSelected: (Product) -> Unit,
    products: List<Product>
) {
    Column(
        modifier = Modifier.padding(horizontal = LocalSpacing.current.medium)
    ) {
        LazyColumn {
            items(products.size) { index ->
                ProductItem(
                    product = products[index],
                    onProductSelected = { product ->
                        onProductSelected(product)
                    }
                )
            }
        }
    }
}

@Composable
private fun ProductItem(
    product: Product,
    onProductSelected: (Product) -> Unit
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
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W500)
                )

                Text(
                    text = "Cantidad: ${product.stock}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                modifier = Modifier
                    .weight(0.3f)
                    .align(Alignment.CenterVertically),
                text = product.price.getBalancedFormatted(),
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
            onClick = { onProductSelected(product) },
            icon = Icons.Outlined.ArrowForward,
            contentDescription = "Detalle"
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ProductItemPreview() {
    MaterialTheme {
        ProductItem(
            product = Product(
                id = "2",
                name = "Pringles Pequenas",
                imageUrl = "https://www.licoresmundiales.com/pub/media/catalog/product/cache/6d1a366ec4c28c9c58646a10a644e322/p/r/pringles-original-1.jpg",
                price = 1.00,
                description = "Pringles Pequenas",
                category = "Dulces",
                stock = 10,
                cartQuantity = 0,
                purchasePrice = 2.00
            ),
            onProductSelected = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AddSaleContentPreview() {
    MaterialTheme {
        InventoryContent(
            onProductSelected = {},
            products = listOf(
                Product(
                    id = "1",
                    name = "Pringles Grandes",
                    price = 2.50,
                    imageUrl = "https://www.licoresmundiales.com/pub/media/catalog/product/cache/6d1a366ec4c28c9c58646a10a644e322/p/r/pringles-original-1.jpg",
                    description = "Coca Cola 2L",
                    category = "Dulces",
                    stock = 10,
                    cartQuantity = 0,
                    purchasePrice = 2.00
                ),
                Product(
                    id = "2",
                    name = "Pringles Pequenas",
                    imageUrl = "https://www.licoresmundiales.com/pub/media/catalog/product/cache/6d1a366ec4c28c9c58646a10a644e322/p/r/pringles-original-1.jpg",
                    price = 1.00,
                    description = "Pringles Pequenas",
                    category = "Dulces",
                    stock = 10,
                    cartQuantity = 0,
                    purchasePrice = 2.00
                ),
                Product(
                    id = "3",
                    name = "Milka",
                    imageUrl = "",
                    price = 2.00,
                    description = "Milka",
                    category = "Chocolate",
                    stock = 10,
                    cartQuantity = 4,
                    purchasePrice = 2.00
                ),
                Product(
                    id = "4",
                    name = "Nutella",
                    imageUrl = "",
                    price = 2.00,
                    description = "Nutella",
                    category = "Chocolate",
                    stock = 10,
                    cartQuantity = 2,
                    purchasePrice = 2.00
                ),
            )
        )
    }
}