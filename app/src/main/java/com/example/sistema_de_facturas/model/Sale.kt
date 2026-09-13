package com.example.sistema_de_facturas.model

/**
 * Data model representing a sale document.
 * Includes a computed property to get the exact total.
 */
data class Sale(
    val id: Long? = null,
    val code: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val type: String, // "BOLETA" or "FACTURA"
    val saleDate: String
) {
    // Calculated sale total: price * quantity
    val total: Double
        get() = price * quantity
}
