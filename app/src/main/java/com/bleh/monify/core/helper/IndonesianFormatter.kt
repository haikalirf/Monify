package com.bleh.monify.core.helper

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

public fun indonesianFormatter(
    maxFractions: Int = 2
): NumberFormat {
    val formatter: NumberFormat = NumberFormat.getCurrencyInstance(
        Locale("id", "ID")
    )
    formatter.maximumFractionDigits = maxFractions
    formatter.currency = Currency.getInstance("IDR")

    return formatter
}