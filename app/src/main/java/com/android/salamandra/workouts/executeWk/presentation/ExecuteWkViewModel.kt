package com.android.salamandra.workouts.executeWk.presentation

import android.util.Log
import androidx.compose.material3.Icon
import androidx.lifecycle.SavedStateHandle
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.WEIGHT_MAX
import com.android.salamandra._core.domain.clock.Clock
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.executions.WorkoutExecution
import com.android.salamandra.navArgs
import com.android.salamandra.workouts.commons.domain.WorkoutsRepository
import com.android.salamandra.workouts.executeWk.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.min


@HiltViewModel
class ExecuteWkViewModel @Inject constructor(
    ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
    private val clock: Clock,
    private val workoutsRepository: WorkoutsRepository,
    private val repository: Repository
) :
    BaseViewModel<ExecuteWkState, ExecuteWkIntent, ExecuteWkEvent>(
        ExecuteWkState.initial,
        ioDispatcher
    ) {

    override fun reduce(intent: ExecuteWkIntent) {
        when (intent) {
            is ExecuteWkIntent.Error -> _state.update { it.copy(error = intent.error) }
            is ExecuteWkIntent.CloseError -> _state.update { it.copy(error = null) }

            ExecuteWkIntent.LogAction -> logAction()

            ExecuteWkIntent.ChangeSurveyToSad -> _state.update { it.copy(survey = 0) }
            ExecuteWkIntent.ChangeSurveyToNeutral -> _state.update { it.copy(survey = 1) }
            ExecuteWkIntent.ChangeSurveyToHappy -> _state.update { it.copy(survey = 2) }

            ExecuteWkIntent.EndWorkout -> endWorkout()
            ExecuteWkIntent.EndWorkoutEarly -> endWorkoutEarly()
            ExecuteWkIntent.SkipSet -> skipSet()
            ExecuteWkIntent.DiscardWorkout -> sendEvent(ExecuteWkEvent.EndWorkout)

            ExecuteWkIntent.HideBottomSheet -> _state.update { it.copy(selectedElement = null) }
            is ExecuteWkIntent.ShowBottomSheet -> _state.update { it.copy(selectedElement = intent.setNumber, textFieldSelected = intent.fieldSelected) }
            ExecuteWkIntent.StopWorkout -> _state.update { it.copy(pausedExecution = true) }
            ExecuteWkIntent.ContinueWorkout -> _state.update { it.copy(pausedExecution = false) }


            is ExecuteWkIntent.EditReps -> updateElementReps(intent.newReps)
            is ExecuteWkIntent.EditWeight -> updateElementWeight(intent.newWeight)
            is ExecuteWkIntent.EditRest -> updateElementRest(intent.newRest)
            is ExecuteWkIntent.AddRest -> incrementRest()

            is ExecuteWkIntent.ChangeActiveTab -> _state.update { it.copy(scaffoldTab = intent.newTab) }
        }
    }

    init {
        val navArgs: ExecuteWkNavArgs = savedStateHandle.navArgs()
        ioLaunch {
            when (val workoutTemplate = workoutsRepository.getWkTemplate(navArgs.wkTemplateId)) {
                is Result.Success -> {
                    val workoutExecutionExercises =
                        workoutTemplate.data.elements.map { it.toWkExecutionExercise() }

                    _state.update {
                        it.copy(
                            exerciseList = workoutExecutionExercises,
                            startOfSetCurrentTimeMillis = clock.currentTimeMillis(),
                            workoutTemplateId = navArgs.wkTemplateId,
                            loading = false
                        )
                    }
                }

                is Result.Error -> _state.update {
                    it.copy(
                        error = workoutTemplate.error,
                        loading = false
                    )
                }
            }
        }
    }

    private fun logAction() {
        val currentSet = state.value.currSet
        val exerciseList = state.value.exerciseList.toMutableList()

        val timeOfSet =
            (clock.currentTimeMillis() - state.value.startOfSetCurrentTimeMillis).toInt()
        _state.update { it.copy(startOfSetCurrentTimeMillis = clock.currentTimeMillis()) }

        // Update time
        val executionElements = exerciseList[state.value.currExercise].executionElements
        val updatedSet = executionElements[currentSet].copy(time = timeOfSet)
        val updatedExecutionElements =
            executionElements.toMutableList().apply { this[currentSet] = updatedSet }
        exerciseList[state.value.currExercise] =
            exerciseList[state.value.currExercise].copy(executionElements = updatedExecutionElements)
        _state.update { it.copy(exerciseList = exerciseList) }

        if (currentSet == _state.value.exerciseList[state.value.currExercise].executionElements.size - 1) {
            if (state.value.currExercise == exerciseList.size - 1) _state.update {
                it.copy(finishedExecution = true)
            } // End Execution
            else _state.update { // Next Exercise
                it.copy(
                    currExercise = state.value.currExercise + 1,
                    currSet = 0
                )
            }
        } else _state.update { it.copy(currSet = currentSet + 1) } // Next rep
    }

    private fun skipSet() {
        val updatedExecutionElement =
            state.value.exerciseList[state.value.currExercise].executionElements.toMutableList()
                .apply { this.removeAt(state.value.currSet) }
        for (i in (state.value.currSet..<updatedExecutionElement.size)) {
            updatedExecutionElement[i] = updatedExecutionElement[i].copy(setNumber = i + 1)
        }

        val updatedList = state.value.exerciseList.toMutableList().apply {
            this[state.value.currExercise] =
                state.value.exerciseList[state.value.currExercise].copy(executionElements = updatedExecutionElement)
        }

        val nextExercise =
            state.value.currSet == updatedExecutionElement.size && state.value.currExercise < updatedList.size - 1

        val workoutEnded =
            state.value.currSet == updatedExecutionElement.size && state.value.currExercise == updatedList.size - 1

        var deletedExercise = false
        if (updatedList[state.value.currExercise].executionElements.isEmpty()) {
            updatedList.removeAt(state.value.currExercise)
            deletedExercise = true
        }

        _state.update {
            it.copy(
                exerciseList = updatedList,
                currExercise =
                if (deletedExercise) state.value.currExercise
                else if (nextExercise) state.value.currExercise + 1
                else state.value.currExercise,
                finishedExecution = workoutEnded,
                currSet = if (nextExercise) 0 else state.value.currSet
            )
        }
    }


    private fun updateElementReps(newReps: Int) {
        if (newReps > Short.MAX_VALUE) {
            return
        }
        val selectedElement = state.value.selectedElement
        val currentExercise = state.value.exerciseList[state.value.currExercise]

        if (selectedElement != null) {
            val updatedElements = currentExercise.executionElements.toMutableList().apply {
                this[selectedElement] = this[selectedElement].copy(reps = newReps)
            }
            val updatedCurrentExercise = currentExercise.copy(executionElements = updatedElements)

            val updatedExerciseList = state.value.exerciseList.toMutableList().apply {
                this[state.value.currExercise] = updatedCurrentExercise
            }

            _state.update { it.copy(exerciseList = updatedExerciseList) }
        }
    }

    private fun updateElementWeight(newWeight: Double) {
        if (newWeight > WEIGHT_MAX) {
            return
        }
        val selectedElement = state.value.selectedElement
        if (selectedElement != null) {
            val currentExercise = state.value.exerciseList[state.value.currExercise]
            val updatedElements = currentExercise.executionElements.toMutableList().apply {
                this[selectedElement] = this[selectedElement].copy(weight = newWeight)
            }
            val updatedCurrentExercise = currentExercise.copy(executionElements = updatedElements)

            val updatedExerciseList = state.value.exerciseList.toMutableList().apply {
                this[state.value.currExercise] = updatedCurrentExercise
            }

            _state.update { it.copy(exerciseList = updatedExerciseList) }
        }
    }

    private fun updateElementRest(newRest: Int) {
        if (newRest > Short.MAX_VALUE) {
            return
        }
        val selectedElement = state.value.selectedElement
        val currentExercise = state.value.exerciseList[state.value.currExercise]

        if (selectedElement != null) {
            val updatedElements = currentExercise.executionElements.toMutableList().apply {
                this[selectedElement] = this[selectedElement].copy(rest = newRest)
            }
            val updatedCurrentExercise = currentExercise.copy(executionElements = updatedElements)

            val updatedExerciseList = state.value.exerciseList.toMutableList().apply {
                this[state.value.currExercise] = updatedCurrentExercise
            }

            _state.update { it.copy(exerciseList = updatedExerciseList) }
        }
    }

    private fun incrementRest() {
        val currentExercise = state.value.exerciseList[state.value.currExercise]
        val currentSet = state.value.currSet
        if (currentSet !in 0..<currentExercise.executionElements.size)
            return
        val rest = currentExercise.executionElements[currentSet].rest
        val newRest = min(rest + 15, Short.MAX_VALUE.toInt())

        val updatedElements = currentExercise.executionElements.toMutableList().apply {
            this[currentSet] = this[currentSet].copy(rest = newRest)
        }
        val updatedCurrentExercise = currentExercise.copy(executionElements = updatedElements)

        val updatedExerciseList = state.value.exerciseList.toMutableList().apply {
            this[state.value.currExercise] = updatedCurrentExercise
        }

        _state.update { it.copy(exerciseList = updatedExerciseList) }
    }



    private fun endWorkout() {
        ioLaunch {
            val workoutExecution = WorkoutExecution(
                date = LocalDate.now(),
                survey = state.value.survey,
                elements = state.value.exerciseList
            )
            when (val creation = repository.createWorkoutExecution(
                state.value.workoutTemplateId,
                workoutExecution = workoutExecution
            )) {
                is Result.Success -> sendEvent(ExecuteWkEvent.EndWorkout)
                is Result.Error -> _state.update { it.copy(error = creation.error) }
            }
        }
    }
    private fun endWorkoutEarly() {
        val setNumber0 = state.value.currSet == 0
        // Empty execution
        if (setNumber0 && state.value.currExercise == 0) {
            sendEvent(ExecuteWkEvent.EndWorkout)
        }
        val cutOff = state.value.currExercise + (if (setNumber0) 0 else 1)
        var updatedExercises = state.value.exerciseList.take(cutOff).toMutableList()
        if (!setNumber0) {
            val updateSet =
                state.value.exerciseList[state.value.currExercise].executionElements.take(state.value.currSet)
            val updatedExercise = updatedExercises[state.value.currExercise].copy(executionElements = updateSet)

            updatedExercises[state.value.currExercise] = updatedExercise
        }
        _state.update { it.copy(exerciseList = updatedExercises, finishedExecution = true, pausedExecution = false) }
    }

}