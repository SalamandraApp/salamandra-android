package com.android.salamandra._core.domain.model

import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import org.junit.Test

class DataModelsTest{
    @Test
    fun `Creating a Workout Template must initialize with correct default values`(){
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