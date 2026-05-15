package org.transactions.currency.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import org.transactions.currency.gateway.CurrencyGatewayImpl
import org.transactions.currency.model.TransactionCurrencyResponse
import org.transactions.currency.model.TransactionRequest
import org.transactions.currency.repository.TransactionRepository
import java.math.BigDecimal
import java.time.format.DateTimeFormatter


@Service
class TransactionService(
    private val repository: TransactionRepository,
    private val currencyGatewayImpl: CurrencyGatewayImpl
) {
    fun create(request: TransactionRequest) = repository.save(request.toEntity())
    fun getTransactionWithExchangeRate(id: Long, targetCurrency: String): TransactionCurrencyResponse {
        val transaction = repository.findById(id).orElseThrow({
            ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "TransactionId not found"
            )
        })

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = transaction.creationDate.minusMonths(6).format(formatter)

        val filter: String = "record_date:gte:${formattedDate},currency:eq:${targetCurrency}&sort:-record_date"
        val exchangeRate = currencyGatewayImpl.getValueAtCurrency(filter).get(0)
        println(exchangeRate)
        return TransactionCurrencyResponse(
            id = 5,
            originalValue = BigDecimal.TEN,
            description = "yyy",
            exchangeRate = BigDecimal.TEN,
            convertedValue = BigDecimal.TEN,
            creationDate = transaction.creationDate

        )
    }

}

