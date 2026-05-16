package org.transactions.currency.model

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import org.transactions.currency.model.entity.Transaction
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

data class TransactionRequest(

    @field:Size(min = 1, max = 50, message = "Description must be between 1 and 50 characters")
    val description: String,
    @field:Positive(message = "Amount must be a positive value")
    val amount: BigDecimal,
    @field:Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "creationDate must be in the format yyyy-MM-dd")
    val creationDate: String,

    ) {
    fun toEntity(): Transaction =
        Transaction(
            description = description,
            amount = amount.setScale(2, RoundingMode.HALF_UP),
            creationDate = LocalDate.parse(creationDate)
        )

}