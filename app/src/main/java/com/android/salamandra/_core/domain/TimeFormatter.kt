package com.android.salamandra._core.domain

import com.android.salamandra._core.domain.model.enums.TimeIntervalFormat
import java.util.Locale

interface TimeFormatter {

    fun toTimeInterval(totalSeconds: Int, format: TimeIntervalFormat): String
    fun toTimeInterval(totalMilliseconds: Long, format: TimeIntervalFormat): String
}

