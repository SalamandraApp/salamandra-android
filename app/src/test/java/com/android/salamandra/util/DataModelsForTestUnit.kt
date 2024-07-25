package com.android.salamandra.util

import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.User
import com.android.salamandra._core.domain.model.enums.Equipment
import com.android.salamandra._core.domain.model.enums.ExerciseType
import com.android.salamandra._core.domain.model.enums.FitnessGoal
import com.android.salamandra._core.domain.model.enums.FitnessLevel
import com.android.salamandra._core.domain.model.enums.Gender
import com.android.salamandra._core.domain.model.enums.MuscleGroup
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import exercise.ExerciseEntity
import workout.WorkoutTemplateElementEntity
import java.time.LocalDate

val EXAMPLE_USER = User(
    uid = "123",
    username = "vzkz",
    displayName = "Vzkz",
    dateJoined = LocalDate.parse("2022-09-12"),
    dateOfBirth = LocalDate.parse("2004-03-11"),
    height = 183,
    weight = 76.5,
    gender = Gender.Male,
    fitnessGoal = FitnessGoal.Bulking,
    fitnessLevel = FitnessLevel.Amateur
)

val EXAMPLE_EXERCISE_PUSH_UP = Exercise(
    exId = "exex1",
    name = "Push Up",
    mainMuscleGroup = MuscleGroup.Chest,
    secondaryMuscleGroup = MuscleGroup.Triceps,
    necessaryEquipment = Equipment.Dumbbell,
    exerciseType = ExerciseType.Calisthenics
)

val EXAMPLE_EXERCISE_PUSH_UP_ENTITY = ExerciseEntity(
    id = "exex1",
    name = "Push Up",
    mainMuscleGroup = MuscleGroup.Chest.ordinal,
    secondaryMuscleGroup = MuscleGroup.Triceps.ordinal,
    necessaryEquipment = Equipment.Dumbbell.ordinal,
    exerciseType = ExerciseType.Calisthenics.ordinal
)

val EXAMPLE_EXERCISE_SQUAT = Exercise(
    exId = "exex2",
    name = "Squat",
    mainMuscleGroup = MuscleGroup.Quadriceps,
    secondaryMuscleGroup = MuscleGroup.Glutes,
    necessaryEquipment = Equipment.Barbell,
    exerciseType = ExerciseType.FreeWeights
)

val EXAMPLE_EXERCISE_SQUAT_ENTITY = ExerciseEntity(
    id = "exex2",
    name = "Squat",
    mainMuscleGroup = MuscleGroup.Quadriceps.ordinal,
    secondaryMuscleGroup = MuscleGroup.Glutes.ordinal,
    necessaryEquipment = Equipment.Barbell.ordinal,
    exerciseType = ExerciseType.FreeWeights.ordinal
)

val EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP = WkTemplateElement(
    templateElementId = "12345",
    exercise = EXAMPLE_EXERCISE_PUSH_UP,
    reps = 15,
    sets = 4,
    weight = null,
    rest = 120
)

val EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_ENTITY_PUSH_UP = WorkoutTemplateElementEntity(
    id = "12345",
    wkTemplateId = "TEMPORAL",
    exerciseId = EXAMPLE_EXERCISE_PUSH_UP.exId,
    position = 1,
    reps = 15,
    sets = 4,
    weight = null,
    rest = 120
)

val EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT = WkTemplateElement(
    templateElementId = "12",
    exercise = EXAMPLE_EXERCISE_SQUAT,
    reps = 15,
    sets = 4,
    weight = null,
    rest = 120
)

val EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_ENTITY_SQUAT = WorkoutTemplateElementEntity(
    id = "12",
    wkTemplateId = "TEMPORAL",
    exerciseId = EXAMPLE_EXERCISE_SQUAT.exId,
    position = 2,
    reps = 15,
    sets = 4,
    weight = null,
    rest = 120
)




