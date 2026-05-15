package org.transactions.currency.client.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDate

data class CurrencyDateInfo(
    @JsonProperty("record_date")
    val recordDate: LocalDate,
    val country: String,
    val currency: String,
    @JsonProperty("exchange_rate")
    val rate: BigDecimal,

    )