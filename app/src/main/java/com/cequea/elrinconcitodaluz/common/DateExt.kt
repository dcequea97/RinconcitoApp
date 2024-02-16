package com.cequea.elrinconcitodaluz.common

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.getSimpleFormattedDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    return this.format(formatter)
}

fun LocalDateTime.getDateWithMonthName(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    return this.format(formatter)
}