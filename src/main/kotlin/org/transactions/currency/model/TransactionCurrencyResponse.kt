package org.transactions.currency.model

import java.math.BigDecimal
import java.time.LocalDate

data class TransactionCurrencyResponse(
    val id: Long,
    val originalValue: BigDecimal,
    val description: String,
    val creationDate: LocalDate,
    val exchangeRate: BigDecimal,
    val convertedValue: BigDecimal
)
