package org.transactions.currency.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.transactions.currency.client.model.CurrencyDateInfo
import org.transactions.currency.gateway.FindCurrencyService
import org.transactions.currency.model.TransactionCurrencyResponse
import org.transactions.currency.model.TransactionRequest
import org.transactions.currency.model.entity.Transaction
import org.transactions.currency.service.TransactionService
import java.time.LocalDate

@RestController
@RequestMapping("/transactions")
class TransactionController(
    private val service: TransactionService,
    private val currencyService: FindCurrencyService
) {
    @GetMapping("/currency")
    fun getCurency(
        @RequestParam(required = false) date: String?,
        @RequestParam(required = false) currency: String = "Real"
    ): ResponseEntity<List<CurrencyDateInfo>> {
        val recordDate = date ?: LocalDate.now().toString()
        val filter = "record_date:eq:$recordDate,currency:eq:$currency"
        val currencies = currencyService.getValueAtCurrency(filter)
        return ResponseEntity.ok(currencies)
    }

    @GetMapping
    fun get(@RequestParam(required = false) id: Long): ResponseEntity<TransactionCurrencyResponse> {
        val transactionResponse = service.getTransactionWithExchangeRate(id, "Real")
        return ResponseEntity.ok(transactionResponse)
    }

    @Operation(summary = "Insert USD transaction", description = "Insert transaction")
    @PostMapping
    fun create(@RequestBody request: TransactionRequest): ResponseEntity<Transaction> {
        val savedEntity = service.create(request)
        return ResponseEntity.ok(savedEntity)
    }
}
