package com.cequea.elrinconcitodaluz.ui.screens.sales.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cequea.elrinconcitodaluz.common.LocalColor
import com.cequea.elrinconcitodaluz.common.LocalSpacing
import com.cequea.elrinconcitodaluz.domain.model.PaymentStatus
import com.cequea.elrinconcitodaluz.domain.model.Product
import com.cequea.elrinconcitodaluz.ui.common.AppIconButton
import com.cequea.elrinconcitodaluz.ui.common.InputForm
import com.cequea.elrinconcitodaluz.ui.common.LoadingProgressBar
import com.cequea.elrinconcitodaluz.ui.screens.sales.SaleEvents
import com.cequea.elrinconcitodaluz.ui.screens.sales.SaleViewModel
import com.google.accompanist.pager.VerticalPagerIndicator
import kotlinx.coroutines.launch

@Composable
fun AddSaleScreen(
    navController: NavHostController,
    viewModel: SaleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(key1 = errorMessage){
        errorMessage?.let {
            viewModel.onEvent(SaleEvents.OnErrorMessageHandled)
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = uiState.saleRegistered){
        if (uiState.saleRegistered){
            viewModel.onEvent(SaleEvents.OnSaleRegisteredHandled)
            navController.popBackStack()
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        AddSaleContent(
            onEvent = viewModel::onEvent,
            products = uiState.products,
            personName = uiState.personName,
            paymentStatus = uiState.paymentStatus
        )

        if (isLoading) {
            LoadingProgressBar(modifier = Modifier.align(Alignment.Center))
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AddSaleContent(
    onEvent: (SaleEvents) -> Unit,
    products: List<Product>,
    personName: String,
    paymentStatus: List<PaymentStatus>
) {
    Column(
        modifier = Modifier
            .padding(horizontal = LocalSpacing.current.small)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val focusManager = LocalFocusManager.current

        InputForm(
            text = personName,
            onValueChange = { onEvent(SaleEvents.OnPersonNameChanged(it)) },
            title = "Comprador:",
            placeholder = "Agregar persona",
            focusManager = focusManager,
            icon = Icons.Outlined.PersonAdd
        )

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Productos:",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W700),
                modifier = Modifier
                    .weight(0.9f)
                    .padding(bottom = LocalSpacing.current.small)
            )

            Icon(
                modifier = Modifier.weight(0.1f),
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = "Text Icon"
            )
        }

        AddSaleProducts(
            onEvent = onEvent,
            products = products
        )

        InputForm(
            text = products.sumOf { it.price * it.cartQuantity }.toString(),
            onValueChange = {},
            title = "Total:",
            placeholder = "0.00",
            focusManager = focusManager,
            icon = Icons.Outlined.AttachMoney,
            enabled = false
        )

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Estatus del pago:",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W700),
                modifier = Modifier
                    .weight(0.9f)
                    .padding(bottom = LocalSpacing.current.small)
            )

            Icon(
                modifier = Modifier.weight(0.1f),
                imageVector = Icons.Outlined.CheckCircleOutline,
                contentDescription = "Text Icon"
            )
        }

        val realSize = paymentStatus.size
        val pagerState = rememberPagerState(initialPage = 1) { realSize }

        LaunchedEffect(key1 = pagerState.currentPage) {
            onEvent(SaleEvents.OnPaymentStatusChanged(paymentStatus[pagerState.currentPage]))
        }

        val scope = rememberCoroutineScope()

        Spacer(modifier = Modifier.height(LocalSpacing.current.extraSmall))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            VerticalPager(
                modifier = Modifier
                    .weight(0.9f)
                    .height(70.dp),
                state = pagerState
            ) { page ->
                Box(
                    modifier = Modifier
                        .background(
                            LocalColor.current.grayBackground,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(LocalSpacing.current.small)
                        .height(50.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(LocalSpacing.current.small),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BasicTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.9f),
                            enabled = false,
                            value = paymentStatus[page].name,
                            onValueChange = { },
                            textStyle = MaterialTheme.typography.bodyMedium,
                            singleLine = true,
                        )
                        AppIconButton(
                            modifier = Modifier.weight(0.1f),
                            icon = Icons.Default.SwapVert,
                            contentDescription = "Text Icon",
                            onClick = {
                                scope.launch {
                                    if (page == realSize - 1) {
                                        pagerState.animateScrollToPage(0)
                                    } else {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                }
                            }
                        )
                    }
                }
            }

            VerticalPagerIndicator(
                modifier = Modifier.weight(0.1f),
                pagerState = pagerState,
                pageCount = paymentStatus.size
            )
        }

        Button(
            modifier = Modifier
                .padding(vertical = LocalSpacing.current.medium),
            onClick = { onEvent(SaleEvents.OnRegisterSaleClicked) }
        ) {
            Text(
                modifier = Modifier.padding(LocalSpacing.current.small),
                text = "Registrar venta",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun AddSaleProducts(
    onEvent: (SaleEvents) -> Unit,
    products: List<Product>
) {
    Column(
        modifier = Modifier
            .padding(horizontal = LocalSpacing.current.small)
    ) {
        products.forEach { product ->
            ProductItem(
                modifier = Modifier.padding(bottom = LocalSpacing.current.small),
                product = product,
                onProductClicked = { onEvent(SaleEvents.OnProductClicked(it)) }
            )
        }
    }

}

@Composable
private fun ProductItem(
    modifier: Modifier = Modifier,
    product: Product,
    onProductClicked: (Product) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(LocalSpacing.current.small)
            .height(50.dp)
            .clickable {
                if (product.cartQuantity == 0) {
                    onProductClicked(product.copy(cartQuantity = 1))
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium
            )

            AnimatedVisibility(
                visible = product.cartQuantity > 0,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onProductClicked(product.copy(cartQuantity = product.cartQuantity - 1)) }) {
                        Icon(
                            imageVector = Icons.Rounded.RemoveCircleOutline,
                            contentDescription = "Remove Product",
                            tint = LocalColor.current.expense
                        )
                    }

                    Text(text = product.cartQuantity.toString())

                    IconButton(onClick = { onProductClicked(product.copy(cartQuantity = product.cartQuantity + 1)) }) {
                        Icon(
                            imageVector = Icons.Rounded.AddCircleOutline,
                            contentDescription = "Add Product",
                            tint = LocalColor.current.income
                        )
                    }
                }
            }

        }
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
            onProductClicked = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AddSaleContentPreview() {
    MaterialTheme {
        AddSaleContent(
            onEvent = {},
            personName = "Luz",
            paymentStatus = listOf(
                PaymentStatus(1, "Pendiente"),
                PaymentStatus(2, "Pagado"),
            ),
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