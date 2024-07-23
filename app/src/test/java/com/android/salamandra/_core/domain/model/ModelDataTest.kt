package com.android.salamandra._core.domain.model

import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import org.junit.Test

class DataModelsTest{
    @Test
    fun `Creating a Workout Template must initialize with correct default values`(){
        //TODO decide whether to use default values or make nullable
        val expectedValue = WorkoutTemplate(
            wkId = "",
            name = "New Workout",
            elements = emptyList(),
            description = null,
            dateCreated = null
        )
        assert(WorkoutTemplate() == expectedValue)
    }
}