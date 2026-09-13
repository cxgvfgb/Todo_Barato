package com.example.sistema_de_facturas.util

import android.util.Log
import com.example.sistema_de_facturas.model.Sale

object SaleValidator {

    private const val TAG = "SaleValidator"

    /**
     * Validates a sale model based on business rules.
     * Returns a Result indicating success or a specific error message.
     */
    fun validate(sale: Sale): ValidationResult {
        // Validating sale
        Log.i(TAG, "Validating sale")

        if (sale.code.trim().isEmpty()) {
            return ValidationResult.Invalid("Code is required and cannot be empty")
        }
        if (sale.name.trim().isEmpty()) {
            return ValidationResult.Invalid("Product name is required and cannot be empty")
        }
        if (sale.price <= 0) {
            return ValidationResult.Invalid("Price must be greater than zero")
        }
        if (sale.quantity <= 0) {
            return ValidationResult.Invalid("Quantity must be greater than zero")
        }
        if (sale.type != "BOLETA" && sale.type != "FACTURA") {
            return ValidationResult.Invalid("Document type must be BOLETA or FACTURA")
        }
        if (sale.saleDate.trim().isEmpty()) {
            return ValidationResult.Invalid("Sale date is required and cannot be empty")
        }

        return ValidationResult.Valid
    }
}

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val errorMessage: String) : ValidationResult()
}
