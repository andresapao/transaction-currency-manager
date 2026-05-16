package org.transactions.currency.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
    @Operation(summary = "Get transactions by id")
    @GetMapping("/{id}")
    fun getId(
        @PathVariable id: Long,
        @Parameter(description = "currency (default Real)", example = "Brazil-Real")
        @RequestParam targetCurrency: String = "Brazil-Real"
    ): ResponseEntity<TransactionCurrencyResponse> {
        val transactionResponse = service.getTransactionWithExchangeRate(id, targetCurrency)
        return ResponseEntity.ok(transactionResponse)
    }

    @Operation(summary = "Get transactions by date")
    @GetMapping
    fun getByDateRange(
        @RequestParam(required = true) startDate: LocalDate,
        @RequestParam(required = true) endDate: LocalDate,
        @Parameter(description = "currency (default Real)", example = "Brazil-Real")
        @RequestParam targetCurrency: String = "Brazil-Real"
    ): ResponseEntity<List<TransactionCurrencyResponse>> {
        val transactionResponse = service.getTransactionsByDate(startDate, endDate, targetCurrency)
        return ResponseEntity.ok(transactionResponse)
    }

    @Operation(summary = "Insert USD transaction")
    @PostMapping
    fun create(@RequestBody @Valid request: TransactionRequest): ResponseEntity<Transaction> {
        val savedEntity = service.create(request)
        return ResponseEntity.ok(savedEntity)
    }
}
