package com.cequea.elrinconcitodaluz.ui.screens.inventory

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material.icons.outlined.PriceCheck
import androidx.compose.material.icons.outlined.ProductionQuantityLimits
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.cequea.elrinconcitodaluz.common.CurrencyAmountVisualTransformation
import com.cequea.elrinconcitodaluz.common.LocalColor
import com.cequea.elrinconcitodaluz.common.LocalSpacing
import com.cequea.elrinconcitodaluz.ui.common.InputForm
import com.cequea.elrinconcitodaluz.ui.common.LoadingProgressBar
import com.cequea.elrinconcitodaluz.ui.theme.AppTheme

@Composable
fun InventoryProductDetailScreen(
    viewModel: InventoryViewModel
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
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        InventoryProductDetailContent(
            onEvent = viewModel::onEvent,
            name = uiState.name,
            category = uiState.category,
            price = uiState.price,
            quantity = uiState.quantity,
            percentageDistribution = uiState.percentageDistribution
        )

        if (isLoading) {
            LoadingProgressBar(modifier = Modifier.align(Alignment.Center))
        }
    }

}

@Composable
fun InventoryProductDetailContent(
    onEvent: (InventoryEvents) -> Unit,
    name: String,
    category: String,
    price: String,
    quantity: String,
    percentageDistribution: Map<String, Double>
) {
    Column(
        modifier = Modifier
            .padding(horizontal = LocalSpacing.current.small)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val focusManager = LocalFocusManager.current

        InputForm(
            text = name,
            onValueChange = { onEvent(InventoryEvents.OnPersonChanged(it)) },
            title = "Nombre del producto:",
            placeholder = "Producto",
            focusManager = focusManager,
            icon = Icons.Outlined.ProductionQuantityLimits
        )

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        InputForm(
            text = category,
            onValueChange = { onEvent(InventoryEvents.OnCategoryChanged(it)) },
            title = "Nombre de la categoria:",
            placeholder = "Categoria",
            focusManager = focusManager,
            icon = Icons.Outlined.Category
        )

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        InputForm(
            text = price,
            onValueChange = { onEvent(InventoryEvents.OnPriceChanged(it)) },
            title = "Precio del producto:",
            placeholder = "Precio",
            focusManager = focusManager,
            icon = Icons.Outlined.PriceCheck
        )

        Spacer(modifier = Modifier.height(LocalSpacing.current.medium))

        InputForm(
            text = quantity,
            onValueChange = { onEvent(InventoryEvents.OnQuantityChanged(it)) },
            title = "Cantidad disponible del producto:",
            placeholder = "Cantidad",
            focusManager = focusManager,
            icon = Icons.Outlined.Numbers
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
                text = "Distribucion:",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W700),
                modifier = Modifier
                    .weight(0.9f)
                    .padding(bottom = LocalSpacing.current.small)
            )

            Icon(
                modifier = Modifier.weight(0.1f),
                imageVector = Icons.Outlined.Percent,
                contentDescription = "Text Icon"
            )
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.small))

        percentageDistribution.forEach { (key, value) ->
            PercentageDistributionItem(
                key = key,
                value = value,
                onValueChange = { onEvent(InventoryEvents.OnPercentageDistributionChanged(key, it)) }
            )
        }

        Spacer(modifier = Modifier.height(LocalSpacing.current.small))

        Button(onClick = { onEvent(InventoryEvents.OnSaveProductClicked) }) {
            Text(
                modifier = Modifier.padding(LocalSpacing.current.small),
                text = "Guardar Cambios",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun PercentageDistributionItem(
    key: String,
    value: Double,
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSpacing.current.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = key, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W700))

        Box(
            modifier = Modifier
                .background(LocalColor.current.grayBackground, shape = RoundedCornerShape(15.dp))
                .padding(LocalSpacing.current.small)
        ) {
            BasicTextField(
                modifier = Modifier,
                value = value.toString(),
                onValueChange = { newValue ->
                    newValue.filter { it.isDigit() }.let(onValueChange)
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                decorationBox = { innerTextField ->
                    innerTextField()
                    if (value.toString().isEmpty()) {
                        Text(
                            text = "Ingresar",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun InventoryProductDetailScreenPreview() {
    AppTheme {
        InventoryProductDetailContent(
            onEvent = {},
            name = "Producto",
            category = "Categoria",
            price = "100",
            quantity = "10",
            percentageDistribution = mapOf(
                "Distribucion 1" to 10.0,
                "Distribucion 2" to 20.0,
                "Distribucion 3" to 30.0
            )
        )
    }
}