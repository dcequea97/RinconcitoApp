package com.cequea.elrinconcitodaluz.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cequea.elrinconcitodaluz.common.LocalSpacing
import com.cequea.elrinconcitodaluz.ui.theme.AppTheme

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    tittle: String,
    onBackClicked: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LocalSpacing.current.medium, vertical = 14.dp)
    ) {
        AppIconButton(
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = onBackClicked,
            icon = Icons.Default.ChevronLeft,
            contentDescription = "Back"
        )

        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = LocalSpacing.current.extraLarge)
                .fillMaxWidth(),
            text = tittle,
            style = MaterialTheme.typography.titleLarge.copy(
                textAlign = TextAlign.Center
            )
        )


    }
}

@Composable
@Preview(showBackground = true)
fun AppTopBarPreview() {
    AppTheme {
        AppTopBar(
            tittle = "Tittle",
            onBackClicked = {}
        )
    }
}