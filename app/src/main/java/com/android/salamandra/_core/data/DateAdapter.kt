package com.android.salamandra._core.data

import android.os.Build
import androidx.annotation.RequiresApi
import app.cash.sqldelight.ColumnAdapter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DateAdapter @Inject constructor(): ColumnAdapter<LocalDate, String> {

    override fun decode(databaseValue: String): LocalDate {
        return LocalDate.parse(databaseValue)
    }

    override fun encode(value: LocalDate): String {
        return value.toString()
    }
}