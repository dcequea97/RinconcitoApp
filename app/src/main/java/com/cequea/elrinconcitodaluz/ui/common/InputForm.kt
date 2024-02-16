package com.cequea.elrinconcitodaluz.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cequea.elrinconcitodaluz.common.LocalColor
import com.cequea.elrinconcitodaluz.common.LocalSpacing

@Composable
fun InputForm(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    title: String,
    placeholder: String,
    maxCharacters: Int = 50,
    icon: ImageVector? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    focusManager: FocusManager,
    keyboardActions: KeyboardActions = KeyboardActions(
        onNext = {
            focusManager.moveFocus(FocusDirection.Down)
        },
        onDone = {
            focusManager.clearFocus()
        }
    ),
    enabled: Boolean = true
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSpacing.current.small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W700),
                modifier = Modifier
                    .weight(0.9f)
                    .padding(bottom = LocalSpacing.current.small)
            )

            icon?.let { icon ->
                Icon(
                    modifier = Modifier.weight(0.1f),
                    imageVector = icon,
                    contentDescription = "Text Icon"
                )
            }
        }

        Box(
            modifier = Modifier
                .background(LocalColor.current.grayBackground, shape = RoundedCornerShape(15.dp))
                .padding(LocalSpacing.current.small)
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
                    enabled = enabled,
                    value = text,
                    onValueChange = { newValue ->
                        newValue.take(maxCharacters).let(onValueChange)
                    },
                    textStyle = textStyle,
                    visualTransformation = visualTransformation,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        keyboardType = keyboardType,
                        imeAction = imeAction
                    ),
                    keyboardActions = keyboardActions,
                    decorationBox = { innerTextField ->
                        innerTextField()
                        if (text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = textStyle
                            )
                        }
                    }
                )

                icon?.let { icon ->
                    Icon(
                        modifier = Modifier.weight(0.1f),
                        imageVector = icon,
                        contentDescription = "Text Icon"
                    )
                }
            }

        }
    }
}

@Composable
@Preview(showBackground = true)
fun InputFormPreview() {
    InputForm(
        text = "",
        onValueChange = {},
        title = "Comprador",
        placeholder = "Agregar persona",
        focusManager = LocalFocusManager.current,
        icon = Icons.Outlined.PersonAdd
    )
}