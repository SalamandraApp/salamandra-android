package com.android.salamandra._core.data.clock

import com.android.salamandra._core.domain.clock.Clock
import javax.inject.Inject

class ClockImpl @Inject constructor() : Clock {
    override fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}