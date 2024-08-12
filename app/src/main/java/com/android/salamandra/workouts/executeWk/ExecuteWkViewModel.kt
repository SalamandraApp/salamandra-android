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
                            executionExercises = workoutExecutionExercises,
                            currentExercise = firstExercise
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
        val exerciseList = state.value.executionExercises
        if (currentSet == currentExercise?.executionElements?.size) {
            if (currentExercise == exerciseList.last()) _state.update { it.copy(workoutEnded = true) } // End Execution
            else _state.update { // Next Exercise
                it.copy(
                    currentExercise = exerciseList[exerciseList.indexOf(currentExercise) + 1],
                    currentSet = 1
                )
            }
        } else _state.update { it.copy(currentSet = currentSet + 1) } // Next rep
    }

    private fun endWorkout(){
        // TODO Make POST to save execution
        sendEvent(ExecuteWkEvent.EndWorkout)
    }

}