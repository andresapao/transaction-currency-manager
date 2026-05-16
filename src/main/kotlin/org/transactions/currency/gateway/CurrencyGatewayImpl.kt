package org.transactions.currency.gateway

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.transactions.currency.client.model.CurrencyDateInfo
import org.transactions.currency.client.model.CurrencyResponseDTO
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.time.Duration

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
            .timeout(Duration.ofSeconds(3))
            .doOnError { println("Error fetching currency data: ${it.message}") }
            .retryWhen(
                Retry.backoff(3, Duration.ofSeconds(2))
                    .doBeforeRetry { retrySignal ->
                        println(
                            "Retry attempt ${retrySignal.totalRetries() + 1} failed. Reason: ${retrySignal.failure().message}",
                        );
                    }
            )
            .onErrorResume { Mono.empty<CurrencyResponseDTO>() }
            .block()

        return response?.data ?: emptyList()
    }
}

