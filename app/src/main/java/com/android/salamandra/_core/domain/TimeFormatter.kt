package com.android.salamandra._core.domain

import com.android.salamandra._core.domain.model.enums.TimeIntervalFormat
import java.util.Locale

class TimeFormatter {

    fun toTimeInterval(totalSeconds: Int, format: TimeIntervalFormat): String {
        val seconds = totalSeconds % 60
        val totalMinutes = totalSeconds / 60
        val minutes = totalMinutes % 60
        val hours = totalMinutes / 60

        return when (format) {
            TimeIntervalFormat.HHcMMcSS -> String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
            TimeIntervalFormat.MMcSSdmm, TimeIntervalFormat.MMcSS -> String.format(Locale.US, "%d:%02d", totalMinutes, seconds)
            TimeIntervalFormat.MMaSSa -> String.format(Locale.US, "%d'%02d''", totalMinutes, seconds)
            TimeIntervalFormat.SSa -> String.format(Locale.US, "%d''", totalSeconds)
        }
    }

    fun toTimeInterval(totalMilliseconds: Long, format: TimeIntervalFormat): String {
        val totalSeconds = (totalMilliseconds / 1000).toInt()

        return when (format) {
            TimeIntervalFormat.HHcMMcSS -> toTimeInterval(totalSeconds, format)
            TimeIntervalFormat.MMcSSdmm, TimeIntervalFormat.MMcSS -> {
                val milliseconds = (totalMilliseconds % 1000).toInt()
                val baseFormat = toTimeInterval(totalSeconds, format)
                String.format(Locale.US, "%s.%03d", baseFormat, milliseconds)
            }
            TimeIntervalFormat.MMaSSa -> toTimeInterval(totalSeconds, format)
            TimeIntervalFormat.SSa -> toTimeInterval(totalSeconds, format)
        }
    }
}
