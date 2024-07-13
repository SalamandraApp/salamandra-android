package com.android.salamandra._core.data.adapter

import app.cash.sqldelight.ColumnAdapter
import javax.inject.Inject

class IntAdapter @Inject constructor(): ColumnAdapter<Int, Long> {

    override fun decode(databaseValue: Long): Int {
        return databaseValue.toInt()
    }

    override fun encode(value: Int): Long {
        return value.toLong()
    }
}