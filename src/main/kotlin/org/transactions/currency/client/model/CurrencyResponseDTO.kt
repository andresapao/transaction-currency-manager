package org.transactions.currency.client.model

data class CurrencyResponseDTO(
    val data: List<CurrencyDateInfo> = emptyList(),
    val meta: Map<String, Any?>? = null,
    val links: Map<String, Any?>? = null
)