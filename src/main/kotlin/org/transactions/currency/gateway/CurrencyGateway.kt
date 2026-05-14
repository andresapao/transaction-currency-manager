package org.transactions.currency.gateway

import org.transactions.currency.model.CurrencyDTO
import reactor.core.publisher.Flux
import java.time.LocalDate

interface CurrencyGateway {
    fun getValueAtCurrency(refDate: LocalDate, currency: String): Flux<CurrencyDTO>
}