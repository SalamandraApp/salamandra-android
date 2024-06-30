package com.android.salamandra._core.data

import app.cash.sqldelight.ColumnAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DateAdapter @Inject constructor(): ColumnAdapter<Date, String> {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

    override fun decode(databaseValue: String): Date {
        return dateFormat.parse(databaseValue) ?: throw IllegalArgumentException("Invalid date format")
    }

    override fun encode(value: Date): String {
        return dateFormat.format(value)
    }
}