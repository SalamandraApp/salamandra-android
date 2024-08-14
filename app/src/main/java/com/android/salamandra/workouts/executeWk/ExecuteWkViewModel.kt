package com.android.salamandra.workouts.executeWk

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.navArgs
import com.android.salamandra.workouts.commons.domain.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class ExecuteWkViewModel @Inject constructor(
    ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
    private val workoutsRepository: WorkoutsRepository
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
        }
    }

    init {
        val navArgs: ExecuteWkNavArgs = savedStateHandle.navArgs()
        ioLaunch {
            when (val workoutTemplate = workoutsRepository.getWkTemplate(navArgs.wkTemplateId)) {
                is Result.Success -> {
                    val workoutExecutionExercises =
                        workoutTemplate.data.elements.map { it.toWkExecutionExercise() }
                    val firstExercise = workoutExecutionExercises.first()
                    _state.update {
                        it.copy(
                            exerciseList = workoutExecutionExercises,
                            currentExercise = firstExercise,
                            startOfSetCurrentTimeMillis = System.currentTimeMillis()
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

        val currentTime = System.currentTimeMillis()
        val timeOfSet = (currentTime - state.value.startOfSetCurrentTimeMillis).toInt()
        _state.update { it.copy(startOfSetCurrentTimeMillis = currentTime) }

        val indexOfCurrentExercise = exerciseList.indexOf(currentExercise)
        val executionElements = exerciseList[indexOfCurrentExercise].executionElements
        val updatedSet = executionElements[currentSet - 1].copy(time = timeOfSet)
        val updatedExecutionElements = executionElements.toMutableList()
        updatedExecutionElements[currentSet - 1] = updatedSet

        exerciseList[exerciseList.indexOf(currentExercise)] = exerciseList[exerciseList.indexOf(currentExercise)].copy(executionElements = updatedExecutionElements)
        _state.update { it.copy(exerciseList = exerciseList) } // record the time of current set

        _state.update { it.copy(currentExercise = exerciseList[indexOfCurrentExercise]) } // Update Current exercise

        if (currentSet == state.value.currentExercise?.executionElements?.size) {
            if (state.value.currentExercise == exerciseList.last()) _state.update { it.copy(workoutEnded = true) } // End Execution
            else _state.update { // Next Exercise
                it.copy(
                    currentExercise = exerciseList[indexOfCurrentExercise + 1],
                    currentSet = 1
                )
            }
        } else _state.update { it.copy(currentSet = currentSet + 1) } // Next rep
    }

    private fun skipSet(){
        val indexOfExercise = state.value.exerciseList.indexOf(state.value.currentExercise)
        val updatedExecutionElement = state.value.exerciseList[indexOfExercise].executionElements.toMutableList()
        updatedExecutionElement.removeAt(state.value.currentSet - 1)
        for(i in (state.value.currentSet - 1 ..< updatedExecutionElement.size )){
            updatedExecutionElement[i] = updatedExecutionElement[i].copy(currentSet = i + 1)
        }

        val updatedList = state.value.exerciseList.toMutableList()
        updatedList[indexOfExercise] = state.value.exerciseList[indexOfExercise].copy(executionElements = updatedExecutionElement)

        _state.update { it.copy(exerciseList = updatedList, currentExercise = updatedList[indexOfExercise]) }
    }

    private fun endWorkout() {
        // TODO Make POST to save execution
        sendEvent(ExecuteWkEvent.EndWorkout)
    }

}