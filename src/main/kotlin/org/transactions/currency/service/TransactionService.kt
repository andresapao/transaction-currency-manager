package org.transactions.currency.service

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import org.transactions.currency.client.model.CurrencyDateInfo
import org.transactions.currency.gateway.CurrencyGatewayImpl
import org.transactions.currency.model.TransactionCurrencyResponse
import org.transactions.currency.model.TransactionRequest
import org.transactions.currency.model.entity.Transaction
import org.transactions.currency.repository.TransactionRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.time.measureTimedValue


@Service
class TransactionService(
    private val repository: TransactionRepository,
    private val currencyGatewayImpl: CurrencyGatewayImpl
) {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private val cache: Cache<String, CurrencyDateInfo?> = Caffeine.newBuilder()
        .expireAfterWrite(
            60,
            TimeUnit.MINUTES
        )
        .recordStats()
        .build()

    @Cacheable("transactionCache")
    private fun getCurrencyInfo(currencyCode: String, transactionDate: LocalDate): CurrencyDateInfo? {
        return cache.get("$currencyCode-$transactionDate") { key ->
            val filter = "record_date:gte:${
                transactionDate.minusMonths(6).format(formatter)
            },country_currency_desc:eq:$currencyCode&sort=-record_date&page[size]=1"
            val currencyInfo = currencyGatewayImpl.getValueAtCurrency(filter).getOrNull(0)
            if (currencyInfo != null) {
                println("Cache miss for key: $key, storing rate ${currencyInfo?.rate} in cache for date ${transactionDate}")
            } else {
                println("No data found for key: $key")
            }
            currencyInfo
        }
    }

    fun create(request: TransactionRequest) = repository.save(request.toEntity())
    fun getTransactionWithExchangeRate(id: Long, targetCurrency: String): TransactionCurrencyResponse {
        val transaction = repository.findById(id).orElseThrow({
            ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "TransactionId not found"
            )
        })
        return convertTransactionToRate(transaction, targetCurrency)
    }

    fun getTransactionsByDate(
        startDate: LocalDate,
        endDate: LocalDate,
        targetCurrency: String
    ): List<TransactionCurrencyResponse> {
        val transactionList = repository.findByCreationDateBetween(startDate, endDate)
        return transactionList.map { transaction ->
            println("Converting transaction $transaction")
            convertTransactionToRate(transaction, targetCurrency)
        }
    }

    private fun convertTransactionToRate(
        transaction: Transaction,
        targetCurrency: String
    ): TransactionCurrencyResponse {
        val (exchangeRate, elapsedTime) = measureTimedValue {
            getCurrencyInfo(
                targetCurrency,
                transaction.creationDate
            )
        }

        println("Elapsed time for getting rate: " + elapsedTime.inWholeMilliseconds + " ms")
        if (exchangeRate == null)
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Exchange not found"
            )
        return TransactionCurrencyResponse.build(transaction, exchangeRate.rate)
    }
}

