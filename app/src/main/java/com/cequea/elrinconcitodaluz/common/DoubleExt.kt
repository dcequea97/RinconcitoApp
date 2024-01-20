package com.cequea.elrinconcitodaluz.common

fun Double.getBalancedFormatted(): String = "$${"%.2f".format(this)}"