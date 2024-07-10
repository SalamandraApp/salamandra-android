package com.android.salamandra._core.data.adapter

import app.cash.sqldelight.ColumnAdapter
import javax.inject.Inject

class BooleanAdapter @Inject constructor(): ColumnAdapter<Boolean, String> {

    override fun decode(databaseValue: String): Boolean {
        return databaseValue.toBoolean()
    }

    override fun encode(value: Boolean): String {
        return value.toString()
    }
}