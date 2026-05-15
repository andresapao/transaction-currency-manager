package org.transactions.currency.gateway

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.transactions.currency.client.model.CurrencyDateInfo
import org.transactions.currency.client.model.CurrencyResponseDTO

@Component
class CurrencyGatewayImpl(
    private val webClient: WebClient,
    @Value("\${external.fiscal.rates-url}")
    private val ratesUrl: String
) : CurrencyGateway {

    override fun getValueAtCurrency(filter: String): List<CurrencyDateInfo> {
        val response = webClient.get()
            .uri("$ratesUrl?filter={filter}", mapOf("filter" to filter))
            .retrieve()
            .bodyToMono<CurrencyResponseDTO>()
            .block()

        return response?.data ?: emptyList()
    }
}

