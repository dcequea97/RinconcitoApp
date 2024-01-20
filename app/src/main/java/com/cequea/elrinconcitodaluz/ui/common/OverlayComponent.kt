package com.cequea.elrinconcitodaluz.ui.common

/*

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.technotypes.bancrecermovilandroid.R
import com.technotypes.bancrecermovilandroid.common.LocalColor
import com.technotypes.bancrecermovilandroid.common.LocalSpacing
import com.technotypes.bancrecermovilandroid.common.LocalTypography
import com.technotypes.bancrecermovilandroid.ui.theme.AppTheme

@Composable
fun OverlayComponent(
    modifier: Modifier = Modifier,
    title: String,
    body: String,
    firstButtonText: String,
    firstButtonStyle: AppButtonStyle = AppButtonStyle.Filled,
    onFirstButtonClick: () -> Unit,
    secondButtonText: String? = null,
    secondButtonStyle: AppButtonStyle = AppButtonStyle.Outlined,
    onSecondButtonClick: () -> Unit = {},
    onCloseButtonClick: () -> Unit,
    imageResource: Int,
    imageTint: Color,
    isCircleShapeIcon: Boolean = false
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = LocalSpacing.current.medium,
                vertical = LocalSpacing.current.extraLarge
            )
            .clip(RoundedCornerShape(16.dp)),
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = LocalSpacing.current.large,
                    vertical = LocalSpacing.current.large
                )
        ) {
            val (
                infoImage, logoutTitle, logoutLabel, yesButton, noButton
            ) = createRefs()

            val backGroundModifier =
                if (isCircleShapeIcon) Modifier.background(imageTint, CircleShape)
                else Modifier

            Icon(
                modifier = backGroundModifier
                    .size(120.dp)
                    .constrainAs(infoImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                painter = painterResource(imageResource),
                tint = if (isCircleShapeIcon) Color.White else imageTint,
                contentDescription = "Boton copiar"
            )

            Text(
                modifier = Modifier
                    .constrainAs(logoutTitle) {
                        top.linkTo(infoImage.bottom, margin = 16.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    .padding(horizontal = 30.dp),
                textAlign = TextAlign.Center,
                text = title,
                style = LocalTypography.current.text19px
            )

            Text(
                modifier = Modifier
                    .constrainAs(logoutLabel) {
                        top.linkTo(logoutTitle.bottom, margin = 32.dp)
                        end.linkTo(parent.end, margin = 100.dp)
                        start.linkTo(parent.start, margin = 100.dp)
                    }
                    .padding(horizontal = 40.dp),
                textAlign = TextAlign.Center,
                text = body,
                style = LocalTypography.current.text13px.copy(
                    fontSize = 15.sp,
                    color = Color(0xFF6A6A6A)
                ),
            )

            AppButton(
                modifier = Modifier
                    .constrainAs(yesButton) {
                        top.linkTo(logoutLabel.bottom, margin = 30.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                onClick = { onFirstButtonClick() },
                text = firstButtonText,
                textStyle = LocalTypography.current.buttonText,
                textAlign = TextAlign.Center,
                buttonStyle = firstButtonStyle
            )

            secondButtonText?.let {
                AppButton(
                    modifier = Modifier
                        .constrainAs(noButton) {
                            top.linkTo(yesButton.bottom, margin = 10.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    onClick = { onSecondButtonClick() },
                    text = secondButtonText,
                    textStyle = LocalTypography.current.buttonText,
                    textAlign = TextAlign.Center,
                    buttonStyle = secondButtonStyle
                )
            }

        }

    }
}


@Composable
@Preview(showBackground = true)
private fun OverlayComponentPreview() {
    AppTheme {
        OverlayComponent(
            title = "CONTACTO AGREGADO",
            body = "Exitosamente",
            firstButtonText = "CONFIRMAR OPERACION",
            onFirstButtonClick = {},
            imageResource = R.drawable.checkmark_done_circle,
            imageTint = LocalColor.current.checkDone,
            onCloseButtonClick = {}
        )
    }
}*/
