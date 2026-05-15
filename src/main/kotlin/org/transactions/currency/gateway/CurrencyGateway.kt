package org.transactions.currency.gateway

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.transactions.currency.client.model.CurrencyDateInfo

@HttpExchange("\${external.fiscal.rates-url}")
interface CurrencyGateway {
    @GetExchange
    fun getValueAtCurrency(@RequestParam("filter") filter: String): List<CurrencyDateInfo>
}