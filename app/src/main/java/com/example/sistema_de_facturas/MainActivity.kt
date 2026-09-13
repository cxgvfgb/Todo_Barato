package com.example.sistema_de_facturas

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sistema_de_facturas.model.Sale
import com.example.sistema_de_facturas.repository.SalesRepository
import com.example.sistema_de_facturas.util.SaleValidator
import com.example.sistema_de_facturas.util.ValidationResult

class MainActivity : AppCompatActivity() {

    private lateinit var salesRepository: SalesRepository
    private val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Connecting main screen to sales logic
        Log.i(tag, "Connecting main screen to sales logic")
        
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize repository
        salesRepository = SalesRepository(this)

        // Load all sales initially into the interface
        loadSalesIntoUI()
    }

    /**
     * Connecting sales functionality to load all sales from the database.
     */
    fun loadSalesIntoUI() {
        // Loading sales into main screen
        Log.i(tag, "Loading sales into main screen")
        val allSales = salesRepository.getAllSales()
        
        // Calculate total sales dynamically from actual data
        val totalAmount = allSales.sumOf { it.total }
        Log.i(tag, "Total calculated sales amount: $totalAmount")
        
        // TODO: UI Developer can hook their adapter or list viewer here using allSales list
    }

    /**
     * Connecting invoice functionality to filter and load only FACTURAS.
     */
    fun loadInvoicesOnly() {
        val invoices = salesRepository.getSalesByType("FACTURA")
        val invoiceTotal = invoices.sumOf { it.total }
        Log.i(tag, "Total calculated invoices: $invoiceTotal")
        // TODO: UI Developer can update their recycler/cards to display invoices with their custom styles/colors
    }

    /**
     * Connecting receipt functionality to filter and load only BOLETAS.
     */
    fun loadReceiptsOnly() {
        val receipts = salesRepository.getSalesByType("BOLETA")
        val receiptTotal = receipts.sumOf { it.total }
        Log.i(tag, "Total calculated receipts: $receiptTotal")
        // TODO: UI Developer can update their recycler/cards to display receipts with their custom styles/colors
    }

    /**
     * Handles adding a new sale with form validation.
     */
    fun handleAddSale(code: String, name: String, price: Double, quantity: Int, type: String, date: String) {
        val newSale = Sale(code = code, name = name, price = price, quantity = quantity, type = type, saleDate = date)
        
        // Validating sale
        when (val validation = SaleValidator.validate(newSale)) {
            is ValidationResult.Valid -> {
                val resultId = salesRepository.createSale(newSale)
                if (resultId > -1) {
                    Toast.makeText(this, "Sale saved successfully", Toast.LENGTH_SHORT).show()
                    loadSalesIntoUI() // Refresh UI data
                } else {
                    Toast.makeText(this, "Failed to save sale to database", Toast.LENGTH_SHORT).show()
                }
            }
            is ValidationResult.Invalid -> {
                // Show useful error messages when information is invalid
                Toast.makeText(this, validation.errorMessage, Toast.LENGTH_LONG).show()
                Log.w(tag, "Validation failed: ${validation.errorMessage}")
            }
        }
    }

    /**
     * Handles updating an existing sale with validation.
     */
    fun handleUpdateSale(id: Long, code: String, name: String, price: Double, quantity: Int, type: String, date: String) {
        val updatedSale = Sale(id = id, code = code, name = name, price = price, quantity = quantity, type = type, saleDate = date)
        
        when (val validation = SaleValidator.validate(updatedSale)) {
            is ValidationResult.Valid -> {
                val rowsAffected = salesRepository.updateSale(updatedSale)
                if (rowsAffected > 0) {
                    Toast.makeText(this, "Sale updated successfully", Toast.LENGTH_SHORT).show()
                    loadSalesIntoUI() // Refresh UI data
                } else {
                    Toast.makeText(this, "Failed to update sale", Toast.LENGTH_SHORT).show()
                }
            }
            is ValidationResult.Invalid -> {
                Toast.makeText(this, validation.errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Handles deleting a sale.
     */
    fun handleDeleteSale(id: Long) {
        val rowsDeleted = salesRepository.deleteSale(id)
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Sale deleted successfully", Toast.LENGTH_SHORT).show()
            loadSalesIntoUI() // Refresh UI data
        } else {
            Toast.makeText(this, "Failed to delete sale", Toast.LENGTH_SHORT).show()
        }
    }
}
