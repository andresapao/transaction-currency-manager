package org.transactions.currency.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import java.math.BigDecimal
import java.time.LocalDate

data class CurrencyDTO(
    @JsonProperty("record_date")
    val recordDate: LocalDate,
    val country: String,
    val currecny: String,
    @JsonProperty("exchange_rate")
    val rate: BigDecimal,

)
