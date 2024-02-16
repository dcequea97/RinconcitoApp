package com.cequea.elrinconcitodaluz.common

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color


data class AppColor(
    val income: Color = Color(0xFFbacbc5c),
    val expense: Color = Color(0xFFac1d15),

    val grayBackground: Color = Color(0xFFF5F0F0),
)

val LocalColor = compositionLocalOf { AppColor() }