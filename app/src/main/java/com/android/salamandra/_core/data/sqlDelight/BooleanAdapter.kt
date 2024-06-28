package com.android.salamandra._core.data.sqlDelight

import app.cash.sqldelight.ColumnAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class BooleanAdapter @Inject constructor(): ColumnAdapter<Boolean, String> {

    override fun decode(databaseValue: String): Boolean {
        return databaseValue.toBoolean()
    }

    override fun encode(value: Boolean): String {
        return value.toString()
    }
}