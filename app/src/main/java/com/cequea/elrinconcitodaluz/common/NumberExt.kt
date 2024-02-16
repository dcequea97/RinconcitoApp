package com.cequea.elrinconcitodaluz.common

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Double.getBalancedFormatted(): String = "$${"%.2f".format(this)}"

fun Long?.getDateFromLong(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this ?: 0), ZoneId.systemDefault())
}