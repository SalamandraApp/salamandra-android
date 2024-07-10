package com.android.salamandra._core.data.adapter

import app.cash.sqldelight.ColumnAdapter
import java.time.LocalDate
import javax.inject.Inject

class DateAdapter @Inject constructor(): ColumnAdapter<LocalDate, String> {

    override fun decode(databaseValue: String): LocalDate {
        return LocalDate.parse(databaseValue)
    }

    override fun encode(value: LocalDate): String {
        return value.toString()
    }
}