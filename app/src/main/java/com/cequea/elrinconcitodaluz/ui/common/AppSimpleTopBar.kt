package com.cequea.elrinconcitodaluz.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cequea.elrinconcitodaluz.common.LocalSpacing


@Composable
fun AppSimpleTopBar(
    onBackClick: () -> Unit,
    title: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(
            bottomEnd = LocalSpacing.current.large,
            bottomStart = LocalSpacing.current.large
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = LocalSpacing.current.medium,
                    end = LocalSpacing.current.medium,
                    top = LocalSpacing.current.extraMedium,
                    bottom = LocalSpacing.current.extraMedium
                )
        ) {
            AppIconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = { onBackClick() },
                icon = Icons.Default.ChevronLeft,
                contentDescription = "Retroceder"
            )

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}