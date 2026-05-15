package org.transactions.currency.gateway

import org.springframework.stereotype.Service
import org.transactions.currency.model.CurrencyDTO

@Service
class FindCurrencyService(
    private val currencyGateway: CurrencyGateway
) {
    fun getValueAtCurrency(filter: String): List<CurrencyDTO> {
        return currencyGateway.getValueAtCurrency(filter)
    }
}