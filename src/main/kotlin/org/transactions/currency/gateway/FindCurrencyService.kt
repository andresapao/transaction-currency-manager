package org.transactions.currency.gateway

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.transactions.currency.client.CurrencyClient
import org.transactions.currency.model.CurrencyDTO
import reactor.core.publisher.Flux
import java.time.LocalDate

@Service
class FindCurrencyService(
    private val webClient: CurrencyClient

) : CurrencyGateway {
    override fun getValueAtCurrency(refDate: LocalDate, currency: String): Flux<CurrencyDTO> {

        return webClient.currencyWebClient()
            .get()
            .uri("?filter=record_date:eq:2026-03-31,currency:eq:Real")
            .retrieve()
            .bodyToFlux(CurrencyDTO::class.java)

    }}