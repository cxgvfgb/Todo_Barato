package com.example.sistema_de_facturas.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.sistema_de_facturas.database.SalesDatabaseHelper
import com.example.sistema_de_facturas.model.Sale

class SalesRepository(context: Context) {

    private val dbHelper = SalesDatabaseHelper(context)
    private val tag = "SalesRepository"

    // Creating sale
    fun createSale(sale: Sale): Long {
        Log.i(tag, "Saving sale")
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SalesDatabaseHelper.COLUMN_CODE, sale.code)
            put(SalesDatabaseHelper.COLUMN_NAME, sale.name)
            put(SalesDatabaseHelper.COLUMN_PRICE, sale.price)
            put(SalesDatabaseHelper.COLUMN_QUANTITY, sale.quantity)
            put(SalesDatabaseHelper.COLUMN_TYPE, sale.type)
            put(SalesDatabaseHelper.COLUMN_SALE_DATE, sale.saleDate)
        }
        return try {
            val id = db.insert(SalesDatabaseHelper.TABLE_SALES, null, values)
            Log.i(tag, "Database operation completed")
            id
        } catch (e: Exception) {
            Log.e(tag, "Database operation failed", e)
            -1L
        }
    }

    // Loading sales
    fun getAllSales(): List<Sale> {
        Log.i(tag, "Loading sales")
        val salesList = mutableListOf<Sale>()
        val db = dbHelper.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.query(
                SalesDatabaseHelper.TABLE_SALES,
                null, null, null, null, null,
                "${SalesDatabaseHelper.COLUMN_ID} DESC"
            )
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    salesList.add(parseSale(cursor))
                } while (cursor.moveToNext())
            }
            Log.i(tag, "Database operation completed")
        } catch (e: Exception) {
            Log.e(tag, "Database operation failed", e)
        } finally {
            cursor?.close()
        }
        return salesList
    }

    // Loading invoice / receipt data based on type
    fun getSalesByType(type: String): List<Sale> {
        if (type.equals("FACTURA", ignoreCase = true)) {
            Log.i(tag, "Loading invoice")
        } else {
            Log.i(tag, "Loading receipt")
        }
        val salesList = mutableListOf<Sale>()
        val db = dbHelper.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.query(
                SalesDatabaseHelper.TABLE_SALES,
                null,
                "${SalesDatabaseHelper.COLUMN_TYPE} = ?",
                arrayOf(type),
                null, null,
                "${SalesDatabaseHelper.COLUMN_ID} DESC"
            )
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    salesList.add(parseSale(cursor))
                } while (cursor.moveToNext())
            }
            Log.i(tag, "Database operation completed")
        } catch (e: Exception) {
            Log.e(tag, "Database operation failed", e)
        } finally {
            cursor?.close()
        }
        return salesList
    }

    // Updating sale
    fun updateSale(sale: Sale): Int {
        Log.i(tag, "Updating sale")
        if (sale.id == null) return 0
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SalesDatabaseHelper.COLUMN_CODE, sale.code)
            put(SalesDatabaseHelper.COLUMN_NAME, sale.name)
            put(SalesDatabaseHelper.COLUMN_PRICE, sale.price)
            put(SalesDatabaseHelper.COLUMN_QUANTITY, sale.quantity)
            put(SalesDatabaseHelper.COLUMN_TYPE, sale.type)
            put(SalesDatabaseHelper.COLUMN_SALE_DATE, sale.saleDate)
        }
        return try {
            val rows = db.update(
                SalesDatabaseHelper.TABLE_SALES,
                values,
                "${SalesDatabaseHelper.COLUMN_ID} = ?",
                arrayOf(sale.id.toString())
            )
            Log.i(tag, "Database operation completed")
            rows
        } catch (e: Exception) {
            Log.e(tag, "Database operation failed", e)
            0
        }
    }

    // Deleting sale
    fun deleteSale(id: Long): Int {
        Log.i(tag, "Deleting sale")
        val db = dbHelper.writableDatabase
        return try {
            val rows = db.delete(
                SalesDatabaseHelper.TABLE_SALES,
                "${SalesDatabaseHelper.COLUMN_ID} = ?",
                arrayOf(id.toString())
            )
            Log.i(tag, "Database operation completed")
            rows
        } catch (e: Exception) {
            Log.e(tag, "Database operation failed", e)
            0
        }
    }

    private fun parseSale(cursor: Cursor): Sale {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(SalesDatabaseHelper.COLUMN_ID))
        val code = cursor.getString(cursor.getColumnIndexOrThrow(SalesDatabaseHelper.COLUMN_CODE))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(SalesDatabaseHelper.COLUMN_NAME))
        val price = cursor.getDouble(cursor.getColumnIndexOrThrow(SalesDatabaseHelper.COLUMN_PRICE))
        val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(SalesDatabaseHelper.COLUMN_QUANTITY))
        val type = cursor.getString(cursor.getColumnIndexOrThrow(SalesDatabaseHelper.COLUMN_TYPE))
        val saleDate = cursor.getString(cursor.getColumnIndexOrThrow(SalesDatabaseHelper.COLUMN_SALE_DATE))
        return Sale(id, code, name, price, quantity, type, saleDate)
    }
}
