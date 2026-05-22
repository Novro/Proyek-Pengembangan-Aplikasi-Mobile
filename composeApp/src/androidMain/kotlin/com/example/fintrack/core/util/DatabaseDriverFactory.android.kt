package com.example.fintrack.core.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.fintrack.data.local.FinTrackDatabase

/**
 * Android implementation of DatabaseDriverFactory
 * 
 * Menggunakan AndroidSqliteDriver yang membungkus SQLite bawaan Android.
 * Database disimpan di internal storage aplikasi.
 */
actual class DatabaseDriverFactory(
    private val context: Context
) {
    actual fun createDriver(): SqlDriver {
        resetDatabaseIfSchemaIsIncompatible()

        return AndroidSqliteDriver(
            schema = FinTrackDatabase.Schema,
            context = context,
            name = "FinTrack.db"
        )
    }

    private fun resetDatabaseIfSchemaIsIncompatible() {
        val databaseName = "FinTrack.db"
        val databaseFile = context.getDatabasePath(databaseName)

        if (!databaseFile.exists()) return

        val requiredColumns = setOf(
            "id",
            "title",
            "amount",
            "type",
            "category",
            "date",
            "created_at",
            "updated_at",
            "currency"
        )

        val existingColumns = try {
            val db = SQLiteDatabase.openDatabase(
                databaseFile.path,
                null,
                SQLiteDatabase.OPEN_READONLY
            )

            try {
                db.rawQuery("PRAGMA table_info(TransactionEntity)", null).use { cursor ->
                    buildSet {
                        val nameIndex = cursor.getColumnIndex("name")
                        while (cursor.moveToNext()) {
                            add(cursor.getString(nameIndex))
                        }
                    }
                }
            } finally {
                db.close()
            }
        } catch (_: Exception) {
            emptySet()
        }

        if (!existingColumns.containsAll(requiredColumns)) {
            context.deleteDatabase(databaseName)
        }
    }
}
