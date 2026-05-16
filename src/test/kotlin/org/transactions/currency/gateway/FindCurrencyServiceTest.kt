package org.transactions.currency.gateway

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.transactions.currency.client.model.CurrencyDateInfo
import java.math.BigDecimal
import java.time.LocalDate

class FindCurrencyServiceTest : StringSpec({

    val currencyGateway = mockk<CurrencyGateway>()
    val findCurrencyService = FindCurrencyService(currencyGateway)

    "should return data from cache when available" {
        val filter = "USD"
        val cachedData =
            listOf(CurrencyDateInfo(LocalDate.parse("2026-05-15"), "USD", "Real", BigDecimal.valueOf(2.33)))

        every { currencyGateway.getValueAtCurrency(filter) } returns cachedData

        val result = findCurrencyService.getValueAtCurrency(filter)

        result shouldBe cachedData
    }

    "should fetch data from gateway when cache is empty" {
        val filter = "EUR"
        val gatewayData = listOf(CurrencyDateInfo(LocalDate.parse("2026-05-15"), "EUR", "EUR", BigDecimal.valueOf(0.9)))

        every { currencyGateway.getValueAtCurrency(filter) } returns gatewayData

        val result = findCurrencyService.getValueAtCurrency(filter)

        result shouldBe gatewayData
    }

    "should return empty list when gateway returns empty" {
        val filter = "GBP"

        every { currencyGateway.getValueAtCurrency(filter) } returns emptyList()

        val result = findCurrencyService.getValueAtCurrency(filter)

        result shouldBe emptyList()
    }
})
