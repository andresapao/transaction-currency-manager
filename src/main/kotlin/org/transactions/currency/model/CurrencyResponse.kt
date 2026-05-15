package org.transactions.currency.model

data class CurrencyResponse(
    val data: List<CurrencyDTO> = emptyList(),
    val meta: Map<String, Any?>? = null,
    val links: Map<String, Any?>? = null
)
