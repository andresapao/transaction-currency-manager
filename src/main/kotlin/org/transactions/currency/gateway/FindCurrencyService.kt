package org.transactions.currency.gateway

import org.springframework.stereotype.Service
import org.transactions.currency.client.model.CurrencyDateInfo

@Service
class FindCurrencyService(
    private val currencyGateway: CurrencyGateway
) {
    fun getValueAtCurrency(filter: String): List<CurrencyDateInfo> {
        return currencyGateway.getValueAtCurrency(filter)
    }
}