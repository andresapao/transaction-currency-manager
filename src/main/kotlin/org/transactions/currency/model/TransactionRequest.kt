package org.transactions.currency.model

import jakarta.validation.constraints.Size
import org.transactions.currency.model.entity.Transaction
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

data class TransactionRequest(

    @field:Size(min = 1, max = 50)
    val description: String,
    val amount: BigDecimal,
    val creationDate: LocalDate = LocalDate.now(),

    ) {
    fun toEntity(): Transaction =
        Transaction(
            description = description,
            amount = amount.setScale(2, RoundingMode.HALF_UP),
            creationDate = creationDate
        )
}