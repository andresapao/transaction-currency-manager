package org.transactions.currency.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.transactions.currency.gateway.CurrencyGateway
import org.transactions.currency.model.CurrencyDTO
import org.transactions.currency.model.Transaction
import org.transactions.currency.model.TransactionRequest
import org.transactions.currency.service.TransactionService
import java.time.LocalDate
import java.util.Currency

@RestController
@RequestMapping("/transactions")
class TransactionController(private val service: TransactionService, private val currencyService: CurrencyGateway) {
    @GetMapping( "/currency")
    fun getCurency(): ResponseEntity<List<CurrencyDTO>> {
        val currencies = currencyService.getValueAtCurrency(LocalDate.now(), "Real")
        return ResponseEntity.ok(currencies.toStream().toList())
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
