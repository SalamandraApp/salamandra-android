package com.android.salamandra.workouts.executeWk.presentation

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra._core.boilerplate.BaseViewModel
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

            ExecuteWkIntent.SkipSet -> skipSet()

            ExecuteWkIntent.HideBottomSheet -> _state.update { it.copy(selectedElement = null) }

            is ExecuteWkIntent.ShowBottomSheet -> _state.update { it.copy(selectedElement = intent.setNumber) }

            is ExecuteWkIntent.EditReps -> updateElementReps(intent.newReps)

            is ExecuteWkIntent.EditWeight -> updateElementWeight(intent.newWeight)

            is ExecuteWkIntent.EditRest -> updateElementRest(intent.newRest)
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
                            currentExercise = workoutExecutionExercises.first(),
                            startOfSetCurrentTimeMillis = clock.currentTimeMillis(),
                            workoutTemplateId = navArgs.wkTemplateId
                        )
                    }
                }

                is Result.Error -> _state.update { it.copy(error = workoutTemplate.error) }
            }
        }
    }

    private fun logAction() {
        val currentExercise = state.value.currentExercise
        val currentSet = state.value.currentSet
        val exerciseList = state.value.exerciseList.toMutableList()

        val timeOfSet =
            (clock.currentTimeMillis() - state.value.startOfSetCurrentTimeMillis).toInt()
        _state.update { it.copy(startOfSetCurrentTimeMillis = clock.currentTimeMillis()) }

        val indexOfCurrentExercise = exerciseList.indexOf(currentExercise)
        val executionElements = exerciseList[indexOfCurrentExercise].executionElements
        val updatedSet = executionElements[currentSet - 1].copy(time = timeOfSet)
        val updatedExecutionElements = executionElements.toMutableList()
        updatedExecutionElements[currentSet - 1] = updatedSet
        exerciseList[exerciseList.indexOf(currentExercise)] =
            exerciseList[exerciseList.indexOf(currentExercise)].copy(executionElements = updatedExecutionElements)
        _state.update { it.copy(exerciseList = exerciseList) } // record the time of current set

        _state.update { it.copy(currentExercise = exerciseList[indexOfCurrentExercise]) } // Update Current exercise

        if (currentSet == state.value.currentExercise?.executionElements?.size) {
            if (state.value.currentExercise == exerciseList.last()) _state.update {
                it.copy(
                    workoutEnded = true
                )
            } // End Execution
            else _state.update { // Next Exercise
                it.copy(
                    currentExercise = exerciseList[indexOfCurrentExercise + 1],
                    currentSet = 1
                )
            }
        } else _state.update { it.copy(currentSet = currentSet + 1) } // Next rep
    }

    private fun skipSet() {
        val indexOfExercise = state.value.exerciseList.indexOf(state.value.currentExercise)
        val updatedExecutionElement =
            state.value.exerciseList[indexOfExercise].executionElements.toMutableList()
        updatedExecutionElement.removeAt(state.value.currentSet - 1)
        for (i in (state.value.currentSet - 1..<updatedExecutionElement.size)) {
            updatedExecutionElement[i] = updatedExecutionElement[i].copy(setNumber = i + 1)
        }

        val updatedList = state.value.exerciseList.toMutableList()
        updatedList[indexOfExercise] =
            state.value.exerciseList[indexOfExercise].copy(executionElements = updatedExecutionElement)

        val nextExercise =
            state.value.currentSet - 1 == updatedExecutionElement.size && indexOfExercise < updatedList.size - 1

        val workoutEnded =
            state.value.currentSet - 1 == updatedExecutionElement.size && indexOfExercise == updatedList.size - 1

        var exerciseDeleted = false
        if (updatedList[indexOfExercise].executionElements.isEmpty()) {
            updatedList.removeAt(indexOfExercise)
            exerciseDeleted = true
        }

        _state.update {
            it.copy(
                exerciseList = updatedList,
                currentExercise =
                if (exerciseDeleted && !workoutEnded) updatedList[indexOfExercise]
                else if (nextExercise) updatedList[indexOfExercise + 1]
                else if (!workoutEnded) updatedList[indexOfExercise]
                else state.value.currentExercise,
                workoutEnded = workoutEnded,
                currentSet = if (nextExercise) 1 else state.value.currentSet
            )
        }
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

    private fun updateElementReps(newReps: Int) {
        val selectedElement = state.value.selectedElement
        val currentExercise = state.value.currentExercise

        if (selectedElement != null && currentExercise != null) {
            val indexOfExercise = state.value.exerciseList.indexOf(state.value.currentExercise)

            val updatedElements = currentExercise.executionElements.toMutableList().apply {
                this[selectedElement - 1] = this[selectedElement - 1].copy(reps = newReps)
            }
            val updatedCurrentExercise = currentExercise.copy(executionElements = updatedElements)

            val updatedExerciseList = state.value.exerciseList.toMutableList().apply {
                this[indexOfExercise] = updatedCurrentExercise
            }

            _state.update {
                it.copy(
                    currentExercise = updatedCurrentExercise,
                    exerciseList = updatedExerciseList
                )
            }
        }
    }

    private fun updateElementWeight(newWeight: Double) {
        val selectedElement = state.value.selectedElement
        val currentExercise = state.value.currentExercise

        if (selectedElement != null && currentExercise != null) {
            val indexOfExercise = state.value.exerciseList.indexOf(state.value.currentExercise)
            val updatedElements = currentExercise.executionElements.toMutableList().apply {
                this[selectedElement - 1] = this[selectedElement - 1].copy(weight = newWeight)
            }

            val updatedCurrentExercise = currentExercise.copy(executionElements = updatedElements)

            val updatedExerciseList = state.value.exerciseList.toMutableList().apply {
                this[indexOfExercise] = updatedCurrentExercise
            }

            _state.update {
                it.copy(
                    currentExercise = updatedCurrentExercise,
                    exerciseList = updatedExerciseList
                )
            }
        }
    }

    private fun updateElementRest(newRest: Int) {
        val selectedElement = state.value.selectedElement
        val currentExercise = state.value.currentExercise

        if (selectedElement != null && currentExercise != null) {
            val indexOfExercise = state.value.exerciseList.indexOf(state.value.currentExercise)
            val updatedElements = currentExercise.executionElements.toMutableList().apply {
                this[selectedElement - 1] = this[selectedElement - 1].copy(rest = newRest)
            }

            val updatedCurrentExercise = currentExercise.copy(executionElements = updatedElements)

            val updatedExerciseList = state.value.exerciseList.toMutableList().apply {
                this[indexOfExercise] = updatedCurrentExercise
            }

            _state.update {
                it.copy(
                    currentExercise = updatedCurrentExercise,
                    exerciseList = updatedExerciseList
                )
            }
        }
    }

}