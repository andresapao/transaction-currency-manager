package org.transactions.currency.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import org.transactions.currency.gateway.CurrencyGatewayImpl
import org.transactions.currency.model.TransactionCurrencyResponse
import org.transactions.currency.model.TransactionRequest
import org.transactions.currency.repository.TransactionRepository
import java.math.RoundingMode
import java.time.format.DateTimeFormatter
import kotlin.time.measureTimedValue


@Service
class TransactionService(
    private val repository: TransactionRepository,
    private val currencyGatewayImpl: CurrencyGatewayImpl
) {
    //private val logger = KotlinLogging.logger {}
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
        val (exchangeRate, elapsedTime) = measureTimedValue { currencyGatewayImpl.getValueAtCurrency(filter).get(0) }

        println("Elapsed time for getting rate: " + elapsedTime.inWholeMilliseconds + " ms")
        return TransactionCurrencyResponse(
            id = transaction.id!!,
            originalValue = transaction.amount,
            description = transaction.description,
            exchangeRate = exchangeRate.rate,
            convertedValue = (transaction.amount * exchangeRate.rate).setScale(2, RoundingMode.HALF_UP),
            creationDate = transaction.creationDate
        )
    }
}

