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

            ExecuteWkIntent.HideBottomSheet -> _state.update { it.copy(indexOfSelectedElement = null) }

            is ExecuteWkIntent.ShowBottomSheet -> _state.update { it.copy(indexOfSelectedElement = intent.setNumber) }

            is ExecuteWkIntent.EditReps -> updateElementReps(intent.newReps)

            is ExecuteWkIntent.EditWeight -> updateElementWeight(intent.newWeight)

            is ExecuteWkIntent.EditRest -> updateElementRest(intent.newRest)

            is ExecuteWkIntent.ChangeActiveTab -> _state.update { it.copy(activeTab = intent.newTab) }
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
                            workoutTemplateId = navArgs.wkTemplateId
                        )
                    }
                }

                is Result.Error -> _state.update { it.copy(error = workoutTemplate.error) }
            }
        }
    }

    private fun logAction() {
        val currentSet = state.value.currentSet
        val exerciseList = state.value.exerciseList.toMutableList()

        val timeOfSet =
            (clock.currentTimeMillis() - state.value.startOfSetCurrentTimeMillis).toInt()
        _state.update { it.copy(startOfSetCurrentTimeMillis = clock.currentTimeMillis()) }

        val executionElements = exerciseList[state.value.indexOfCurrentExercise].executionElements
        val updatedSet = executionElements[currentSet - 1].copy(time = timeOfSet)
        val updatedExecutionElements =
            executionElements.toMutableList().apply { this[currentSet - 1] = updatedSet }
        exerciseList[state.value.indexOfCurrentExercise] =
            exerciseList[state.value.indexOfCurrentExercise].copy(executionElements = updatedExecutionElements)
        _state.update { it.copy(exerciseList = exerciseList) } // record the time of current set

        if (currentSet == _state.value.exerciseList[state.value.indexOfCurrentExercise].executionElements.size) {
            if (state.value.indexOfCurrentExercise == exerciseList.size - 1) _state.update {
                it.copy(workoutEnded = true)
            } // End Execution
            else _state.update { // Next Exercise
                it.copy(
                    indexOfCurrentExercise = state.value.indexOfCurrentExercise + 1,
                    currentSet = 1
                )
            }
        } else _state.update { it.copy(currentSet = currentSet + 1) } // Next rep
    }

    private fun skipSet() {
        val updatedExecutionElement =
            state.value.exerciseList[state.value.indexOfCurrentExercise].executionElements.toMutableList()
                .apply { this.removeAt(state.value.currentSet - 1) }
        for (i in (state.value.currentSet - 1..<updatedExecutionElement.size)) {
            updatedExecutionElement[i] = updatedExecutionElement[i].copy(setNumber = i + 1)
        }

        val updatedList = state.value.exerciseList.toMutableList().apply {
            this[state.value.indexOfCurrentExercise] =
                state.value.exerciseList[state.value.indexOfCurrentExercise].copy(executionElements = updatedExecutionElement)
        }

        val nextExercise =
            state.value.currentSet - 1 == updatedExecutionElement.size && state.value.indexOfCurrentExercise < updatedList.size - 1

        val workoutEnded =
            state.value.currentSet - 1 == updatedExecutionElement.size && state.value.indexOfCurrentExercise == updatedList.size - 1

        if (updatedList[state.value.indexOfCurrentExercise].executionElements.isEmpty())
            updatedList.removeAt(state.value.indexOfCurrentExercise)

        _state.update {
            it.copy(
                exerciseList = updatedList,
                indexOfCurrentExercise =
                if (nextExercise) state.value.indexOfCurrentExercise + 1
                else state.value.indexOfCurrentExercise,
                workoutEnded = workoutEnded,
                currentSet = if (nextExercise) 1 else state.value.currentSet
            )
        }
    }

    private fun updateElementReps(newReps: Int) {
        val selectedElement = state.value.indexOfSelectedElement
        val currentExercise = state.value.exerciseList[state.value.indexOfCurrentExercise]

        if (selectedElement != null) {
            val updatedElements = currentExercise.executionElements.toMutableList().apply {
                this[selectedElement - 1] = this[selectedElement - 1].copy(reps = newReps)
            }
            val updatedCurrentExercise = currentExercise.copy(executionElements = updatedElements)

            val updatedExerciseList = state.value.exerciseList.toMutableList().apply {
                this[state.value.indexOfCurrentExercise] = updatedCurrentExercise
            }

            _state.update { it.copy(exerciseList = updatedExerciseList) }
        }
    }

    private fun updateElementWeight(newWeight: Double) {
        val selectedElement = state.value.indexOfSelectedElement
        val currentExercise = state.value.exerciseList[state.value.indexOfCurrentExercise]

        if (selectedElement != null) {
            val updatedElements = currentExercise.executionElements.toMutableList().apply {
                this[selectedElement - 1] = this[selectedElement - 1].copy(weight = newWeight)
            }
            val updatedCurrentExercise = currentExercise.copy(executionElements = updatedElements)

            val updatedExerciseList = state.value.exerciseList.toMutableList().apply {
                this[state.value.indexOfCurrentExercise] = updatedCurrentExercise
            }

            _state.update { it.copy(exerciseList = updatedExerciseList) }
        }
    }

    private fun updateElementRest(newRest: Int) {
        val selectedElement = state.value.indexOfSelectedElement
        val currentExercise = state.value.exerciseList[state.value.indexOfCurrentExercise]

        if (selectedElement != null) {
            val updatedElements = currentExercise.executionElements.toMutableList().apply {
                this[selectedElement - 1] = this[selectedElement - 1].copy(rest = newRest)
            }
            val updatedCurrentExercise = currentExercise.copy(executionElements = updatedElements)

            val updatedExerciseList = state.value.exerciseList.toMutableList().apply {
                this[state.value.indexOfCurrentExercise] = updatedCurrentExercise
            }

            _state.update { it.copy(exerciseList = updatedExerciseList) }
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
}