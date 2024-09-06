package com.android.salamandra._core.domain.model.enums

// c for :
// a for ' or ''
// d for .
enum class TimeIntervalFormat {
    HHcMMcSS,           // 1:30:45

    MMcSSdmm,           // 90:45.333
    MMcSS,              // 90:45

    MMaSSa,             // 90'45''
    SSa,                // 5445''
}