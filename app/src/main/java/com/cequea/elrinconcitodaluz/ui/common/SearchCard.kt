package com.cequea.elrinconcitodaluz.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cequea.elrinconcitodaluz.R
import com.cequea.elrinconcitodaluz.common.LocalSpacing


@Composable
fun SearchCard(
    onValueChange: (String) -> Unit,
    value: String,
    modifier: Modifier = Modifier,
    placeholderText: String
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = LocalSpacing.current.small,
                horizontal = LocalSpacing.current.large
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFECECEC)
        ),
        shape = RoundedCornerShape(17.dp)
    ) {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = LocalSpacing.current.medium,
                ),
            placeholder = { Text(text = placeholderText) },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Start,
            ),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            leadingIcon = {
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "Busqueda",
                        modifier = Modifier.size(26.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )

    }
}