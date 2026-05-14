package org.transactions.currency.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionRequest(
    val description: String,
    val amount: BigDecimal,
    val creationDate: LocalDateTime

    )
{
    fun toEntity(): Transaction = Transaction(description = description, amount = amount, creationDate = creationDate)
}