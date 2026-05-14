package org.transactions.currency.service

import org.springframework.stereotype.Service
import org.transactions.currency.model.Transaction
import org.transactions.currency.model.TransactionRequest
import org.transactions.currency.repository.TransactionRepository

@Service
class TransactionService (private val repository: TransactionRepository) {
    fun create(request: TransactionRequest)  = repository.save(request.toEntity())
fun getAll() : List<Transaction> = repository.findAll()

    }

