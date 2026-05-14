package org.transactions.currency

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TransactionsCurrencyManagerApplication

fun main(args: Array<String>) {
    runApplication<TransactionsCurrencyManagerApplication>(*args)
}
