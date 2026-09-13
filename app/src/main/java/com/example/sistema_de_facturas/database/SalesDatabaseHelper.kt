package com.example.sistema_de_facturas.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SalesDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val TAG = "SalesDatabase"
        private const val DATABASE_NAME = "sales_system.db"
        private const val DATABASE_VERSION = 1

        // Table and columns
        const val TABLE_SALES = "sales"
        const val COLUMN_ID = "id"
        const val COLUMN_CODE = "code"
        const val COLUMN_NAME = "name"
        const val COLUMN_PRICE = "price"
        const val COLUMN_QUANTITY = "quantity"
        const val COLUMN_TYPE = "type"
        const val COLUMN_SALE_DATE = "saleDate"

        // Create table query
        private const val CREATE_TABLE_SALES = ("CREATE TABLE " + TABLE_SALES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CODE + " TEXT NOT NULL, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_PRICE + " REAL NOT NULL, "
                + COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + COLUMN_TYPE + " TEXT NOT NULL, "
                + COLUMN_SALE_DATE + " TEXT NOT NULL" + ")")
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Creating database
        Log.i(TAG, "Creating database")
        try {
            db?.execSQL(CREATE_TABLE_SALES)
            Log.i(TAG, "Database operation completed")
        } catch (e: Exception) {
            Log.e(TAG, "Database operation failed", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_SALES")
            onCreate(db)
        } catch (e: Exception) {
            Log.e(TAG, "Database operation failed during upgrade", e)
        }
    }

    override fun getWritableDatabase(): SQLiteDatabase {
        // Opening database
        Log.i(TAG, "Opening database")
        return super.getWritableDatabase()
    }

    override fun getReadableDatabase(): SQLiteDatabase {
        // Opening database
        Log.i(TAG, "Opening database")
        return super.getReadableDatabase()
    }
}
