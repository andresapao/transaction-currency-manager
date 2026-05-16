package org.transactions.currency.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import java.math.BigDecimal

class TransactionRequestTest : StringSpec({

    val validatorFactory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
    val validator: Validator = validatorFactory.validator

    "valid TransactionRequest should pass validation" {
        val request = TransactionRequest(
            description = "Valid Description",
            amount = BigDecimal("100.00"),
            creationDate = "2026-05-16"
        )

        val violations = validator.validate(request)
        violations.size shouldBe 0
    }

    "description too long should fail validation" {
        val request = TransactionRequest(
            description = "A".repeat(51),
            amount = BigDecimal("100.00"),
            creationDate = "2026-05-16"
        )

        val violations = validator.validate(request)
        violations.size shouldBe 1
        violations.first().message shouldBe "Description must be between 1 and 50 characters"
    }

    "negative amount should fail validation" {
        val request = TransactionRequest(
            description = "Valid Description",
            amount = BigDecimal("-100.00"),
            creationDate = "2026-05-16"
        )

        val violations = validator.validate(request)
        violations.size shouldBe 1
        violations.first().message shouldBe "Amount must be a positive value"
    }

    "invalid creationDate format should fail validation" {
        val request = TransactionRequest(
            description = "Valid Description",
            amount = BigDecimal("100.00"),
            creationDate = "16-05-2026"
        )

        val violations = validator.validate(request)
        violations.size shouldBe 1
        violations.first().message shouldBe "creationDate must be in the format yyyy-MM-dd"
    }

    "empty description should fail validation" {
        val request = TransactionRequest(
            description = "",
            amount = BigDecimal("100.00"),
            creationDate = "2026-05-16"
        )

        val violations = validator.validate(request)
        violations.size shouldBe 1
        violations.first().message shouldBe "Description must be between 1 and 50 characters"
    }

    "zero amount should fail validation" {
        val request = TransactionRequest(
            description = "Valid Description",
            amount = BigDecimal("0.00"),
            creationDate = "2026-05-16"
        )

        val violations = validator.validate(request)
        violations.size shouldBe 1
        violations.first().message shouldBe "Amount must be a positive value"
    }

})
