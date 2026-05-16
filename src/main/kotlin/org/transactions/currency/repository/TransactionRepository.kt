package org.transactions.currency.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.transactions.currency.model.entity.Transaction
import java.time.LocalDate

interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findByCreationDateBetween(startDate: LocalDate, endDate: LocalDate): List<Transaction>
}
