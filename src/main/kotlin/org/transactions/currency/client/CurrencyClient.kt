package org.transactions.currency.client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration

class CurrencyClient {

    @Bean
    fun currencyWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange")
            .build()
    }
}