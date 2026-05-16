package org.transactions.currency.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import org.transactions.currency.client.model.CurrencyDateInfo
import org.transactions.currency.gateway.CurrencyGatewayImpl
import org.transactions.currency.model.entity.Transaction
import org.transactions.currency.repository.TransactionRepository
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class TransactionServiceTest : StringSpec({

    val repository = mockk<TransactionRepository>()
    val currencyGatewayImpl = mockk<CurrencyGatewayImpl>()
    val transactionService = TransactionService(repository, currencyGatewayImpl)

    "should get data from cache when available" {
        val currencyCode = "USD"
        val id: Long = 9
        val transactionDate = LocalDate.now()
        val cachedData = CurrencyDateInfo(LocalDate.parse("2026-05-15"), "USD", "Real", BigDecimal.valueOf(2.33))
        every { currencyGatewayImpl.getValueAtCurrency(any()) } returns listOf(cachedData)
        every { repository.findById(id) } returns Optional.of(
            Transaction(
                amount = BigDecimal.TEN,
                id = id,
                description = "teste",
                currency = currencyCode,
                creationDate = transactionDate
            )
        )

        val firstResult = transactionService.getTransactionWithExchangeRate(
            id,
            currencyCode
        )
        val secondResult = transactionService.getTransactionWithExchangeRate(
            id,
            currencyCode
        )
        verify(exactly = 1) { currencyGatewayImpl.getValueAtCurrency(any()) }
        firstResult shouldBeEqual secondResult
    }


    "should throw exception when no data is found" {
        val currencyCode = "USD"
        val id: Long = 9
        val transactionDate = LocalDate.now()

        every { currencyGatewayImpl.getValueAtCurrency(any()) } returns emptyList()
        every { repository.findById(id) } returns Optional.of(
            Transaction(
                amount = BigDecimal.TEN,
                id = id,
                description = "teste",
                currency = currencyCode,
                creationDate = transactionDate
            )
        )

        val exception = shouldThrow<Exception> {
            transactionService.getTransactionWithExchangeRate(9, "Real")
        }

        exception shouldBe ResponseStatusException(HttpStatus.NOT_FOUND, "Exchange not found")
    }
})
