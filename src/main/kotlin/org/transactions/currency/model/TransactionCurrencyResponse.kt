package org.transactions.currency.model

import org.transactions.currency.model.entity.Transaction
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

data class TransactionCurrencyResponse(
    val id: Long,
    val originalValue: BigDecimal,
    val description: String,
    val creationDate: LocalDate,
    val exchangeRate: BigDecimal,
    val convertedValue: BigDecimal
) {
    companion object {
        fun build(transaction: Transaction, exchangeRate: BigDecimal) = TransactionCurrencyResponse(
            id = transaction.id!!,
            originalValue = transaction.amount,
            description = transaction.description,
            exchangeRate = exchangeRate,
            convertedValue = (transaction.amount * exchangeRate).setScale(2, RoundingMode.HALF_UP),
            creationDate = transaction.creationDate
        )
    }

}
