package org.transactions.currency.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.transactions.currency.gateway.CurrencyGateway
import org.transactions.currency.gateway.FindCurrencyService
import org.transactions.currency.model.CurrencyDTO
import org.transactions.currency.model.Transaction
import org.transactions.currency.model.TransactionRequest
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
    ): ResponseEntity<List<CurrencyDTO>> {
        val recordDate = date ?: LocalDate.now().toString()
        val filter = "record_date:eq:$recordDate,currency:eq:$currency"
        val currencies = currencyService.getValueAtCurrency(filter)
        return ResponseEntity.ok(currencies)
    }

    @GetMapping
    fun getDrivers(): ResponseEntity<List<Transaction>> {
        val drivers = service.getAll()
        return ResponseEntity.ok(drivers)
    }

    @PostMapping
    fun create(@RequestBody request: TransactionRequest): ResponseEntity<Transaction> {
        val savedEntity = service.create(request)
        return ResponseEntity.ok(savedEntity)
    }
}
