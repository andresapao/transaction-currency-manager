package org.transactions.currency.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.transactions.currency.model.Transaction

interface TransactionRepository : JpaRepository<Transaction, Long>
