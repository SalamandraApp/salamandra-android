package com.android.salamandra.workouts.editWk.presentation

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra.navArgs
import com.android.salamandra.workouts.editWk.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class EditWkViewModel @Inject constructor(
    ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
    private val repository: Repository
) :
    BaseViewModel<EditWkState, EditWkIntent, EditWkEvent>(EditWkState.initial, ioDispatcher) {

    override fun reduce(intent: EditWkIntent) {
        when (intent) {
            is EditWkIntent.Error -> _state.update { it.copy(error = intent.error) }

            is EditWkIntent.CloseError -> _state.update { it.copy(error = null) }

            EditWkIntent.NavigateToHome -> sendEvent(EditWkEvent.NavigateToHome)

            // Toggle bottom sheet
            is EditWkIntent.HideBottomSheet -> _state.update { it.copy(bottomSheet = false) }

            is EditWkIntent.ShowBottomSheet -> _state.update {
                it.copy(
                    bottomSheet = true,
                    exerciseSelectedIndex = intent.index
                )
            }

            is EditWkIntent.ChangeWkName -> _state.update {
                it.copy(
                    wkTemplate = it.wkTemplate.copy(
                        name = intent.newName
                    )
                )
            }

            is EditWkIntent.ChangeWkDescription -> {
                if (intent.newDescription != "") _state.update {
                    it.copy(wkTemplate = it.wkTemplate.copy(description = intent.newDescription))
                }
                else _state.update {
                    it.copy(wkTemplate = it.wkTemplate.copy(description = null))
                }
            }

            is EditWkIntent.ChangeWkElementReps -> updateReps(intent.index, intent.newReps)

            is EditWkIntent.ChangeWkElementSets -> updateSets(intent.index, intent.newSets)

            is EditWkIntent.ChangeWkElementWeight -> updateWeight(intent.index, intent.newWeight)

            is EditWkIntent.NavigateToSearch -> navigateToSearch()

            EditWkIntent.CreteWorkout -> createWorkout()
        }
    }

    init {
        val navArgs: EditWkNavArgs = savedStateHandle.navArgs()
        ioLaunch {
            _state.update { it.copy(wkTemplate = it.wkTemplate.copy(elements = repository.retrieveSavedWorkoutTemplateElements())) }
            addExercisesToTemplate(repository.getAllExercises(navArgs.addedExercises))
        }
    }

    private fun createWorkout(){
        ioLaunch {
            when(val creation = repository.createWorkout(state.value.wkTemplate)) {
                is Result.Success -> sendEvent(EditWkEvent.NavigateToHome)
                is Result.Error -> _state.update { it.copy(error = creation.error) }
            }
        }

    }

    private fun navigateToSearch(){
        ioLaunch {
            repository.saveWorkoutTemplateElementsTemporary(state.value.wkTemplate.elements)
            sendEvent(EditWkEvent.NavigateToSearch)
        }
    }

    private fun addExercisesToTemplate(exercises: List<Exercise>) {
        val elements = state.value.wkTemplate.elements.toMutableList()
        exercises.forEach { exercise ->
            elements.add(WkTemplateElement(exercise = exercise, position = elements.size + 1))
        }
        _state.update { it.copy(wkTemplate = it.wkTemplate.copy(elements = elements)) }
    }



    private fun updateReps(index: Int, newReps: Int) {
        _state.value.let { currentState ->
            val updatedElements = currentState.wkTemplate.elements.toMutableList()
            val updatedElement = updatedElements[index].copy(reps = newReps)
            updatedElements[index] = updatedElement

            val updatedState = currentState.copy(
                wkTemplate = currentState.wkTemplate.copy(elements = updatedElements)
            )
            _state.value = updatedState
        }
    }

    private fun updateSets(index: Int, newSets: Int) {
        _state.value.let { currentState ->
            val updatedElements = currentState.wkTemplate.elements.toMutableList()
            val updatedElement = updatedElements[index].copy(sets = newSets)
            updatedElements[index] = updatedElement

            val updatedState = currentState.copy(
                wkTemplate = currentState.wkTemplate.copy(elements = updatedElements)
            )
            _state.value = updatedState
        }
    }

    private fun updateWeight(index: Int, newWeight: Double) {
        _state.value.let { currentState ->
            val updatedElements = currentState.wkTemplate.elements.toMutableList()
            val updatedElement = updatedElements[index].copy(weight = newWeight)
            updatedElements[index] = updatedElement

            val updatedState = currentState.copy(
                wkTemplate = currentState.wkTemplate.copy(elements = updatedElements)
            )
            _state.value = updatedState
        }
    }


}