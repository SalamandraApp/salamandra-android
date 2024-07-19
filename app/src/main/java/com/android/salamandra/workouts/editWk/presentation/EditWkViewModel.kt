package com.android.salamandra.workouts.editWk.presentation

import android.util.Log
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

            EditWkIntent.NavigateToHome -> navToHome()
            is EditWkIntent.HideBottomSheet -> _state.update { it.copy(selectedElementIndex = null) }
            is EditWkIntent.ShowBottomSheet -> _state.update { it.copy(selectedElementIndex = intent.index) }

            is EditWkIntent.ChangeWkName -> _state.update { it.copy(wkTemplate = it.wkTemplate.copy(name = intent.newName)) }
            is EditWkIntent.ChangeWkDescription -> {
                if (intent.newDescription != "") _state.update {
                    it.copy(wkTemplate = it.wkTemplate.copy(description = intent.newDescription))
                }
                else _state.update {
                    it.copy(wkTemplate = it.wkTemplate.copy(description = null))
                }
            }

            is EditWkIntent.ChangeSets -> updateWkElementSets(intent.newSets, intent.index)
            is EditWkIntent.ChangeReps -> updateWkElementReps(intent.newReps, intent.index)
            is EditWkIntent.ChangeWeight -> updateWkElementWeight(intent.newWeight, intent.index)
            is EditWkIntent.ChangeRest-> updateWkElementRest(intent.newRest, intent.index)
            is EditWkIntent.DeleteWkElement -> {
                removeWkElement(intent.index)
                _state.update { it.copy(selectedElementIndex = null) }
                // TODO, remove from local DB
            }


            is EditWkIntent.NavigateToSearch -> navigateToSearch()

            EditWkIntent.CreateWorkout -> createWorkout()
        }
    }

    init {
        val navArgs: EditWkNavArgs = savedStateHandle.navArgs()
        ioLaunch {
            _state.update { it.copy(wkTemplate = it.wkTemplate.copy(elements = repository.retrieveSavedWorkoutTemplateElements())) }
            addExercisesToTemplate(repository.getAllExercises(navArgs.addedExercises))
        }
    }

    private fun createWorkout() {
        ioLaunch {
            when (val creation = repository.createWorkout(state.value.wkTemplate)) {
                is Result.Success -> sendEvent(EditWkEvent.NavigateToHome)
                is Result.Error -> _state.update { it.copy(error = creation.error) }
            }
        }

    }

    private fun navToHome() {
        ioLaunch {
            repository.deleteTemporalTemplateElements()
            sendEvent(EditWkEvent.NavigateToHome)
        }
    }

    private fun navigateToSearch() {
        ioLaunch {
            repository.saveWorkoutTemplateElementsTemporarly(state.value.wkTemplate.elements)
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



    private fun updateWkElementReps(newReps: Int, index: Int) {
        val updatedElements = state.value.wkTemplate.elements.toMutableList().apply {
            this[index] = this[index].copy(reps = newReps)
        }
        _state.update {it.copy(wkTemplate = state.value.wkTemplate.copy(elements = updatedElements)) }
    }
    private fun updateWkElementSets(newSets: Int, index: Int) {
        val updatedElements = state.value.wkTemplate.elements.toMutableList().apply {
            this[index] = this[index].copy(sets = newSets)
        }
        _state.update {it.copy(wkTemplate = state.value.wkTemplate.copy(elements = updatedElements)) }
    }
    private fun updateWkElementWeight(newWeight: Double, index: Int) {
        val updatedElements = state.value.wkTemplate.elements.toMutableList().apply {
            this[index] = this[index].copy(weight = newWeight)
        }
        _state.update {it.copy(wkTemplate = state.value.wkTemplate.copy(elements = updatedElements)) }
    }
    private fun updateWkElementRest(newRest: Int, index: Int) {
        val updatedElements = state.value.wkTemplate.elements.toMutableList().apply {
            this[index] = this[index].copy(rest = newRest)
        }
        _state.update {it.copy(wkTemplate = state.value.wkTemplate.copy(elements = updatedElements)) }
    }
    private fun removeWkElement(index: Int) {
        val updatedElements = state.value.wkTemplate.elements.toMutableList().apply {
            if (index in indices) {
                removeAt(index)
            }
        }
        _state.update { it.copy(wkTemplate = state.value.wkTemplate.copy(elements = updatedElements)) }
    }
}