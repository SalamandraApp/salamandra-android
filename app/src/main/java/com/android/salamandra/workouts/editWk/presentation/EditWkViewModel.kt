package com.android.salamandra.workouts.editWk.presentation

import android.util.Log
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.SavedStateHandle
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.WEIGHT_MAX
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.workout.template.WkTemplateElement
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

            is EditWkIntent.HideBottomSheet -> _state.update { it.copy(selectedElementIndex = null, notImplementedBanner = false) }
            is EditWkIntent.ShowNotImplementedBanner -> _state.update { it.copy(notImplementedBanner = true) }
            is EditWkIntent.ShowElementBanner -> _state.update {
                it.copy(selectedElementIndex = intent.index, textFieldSelected = intent.field)
            }

            is EditWkIntent.ChangeWkName -> _state.update {
                it.copy(wkTemplate = it.wkTemplate.copy(name = intent.newName))
            }

            is EditWkIntent.NavigateToHome -> navigateToHome()

            is EditWkIntent.ChangeWkDescription -> changeWkDescription(intent.newDescription)
            is EditWkIntent.ChangeSets -> updateWkElementSets(newSets = intent.newSets)
            is EditWkIntent.ChangeReps -> updateWkElementReps(newReps = intent.newReps)
            is EditWkIntent.ChangeWeight -> updateWkElementWeight(newWeight = intent.newWeight)
            is EditWkIntent.ChangeRest -> updateWkElementRest(newRest = intent.newRest,)
            EditWkIntent.CreateWorkout -> createWorkout()
            is EditWkIntent.DeleteWkElement -> deleteWkElement()

            is EditWkIntent.NavigateToSearch -> navigateToSearch()

        }
    }

    init {
        val navArgs: EditWkNavArgs = savedStateHandle.navArgs()
        ioLaunch {
            _state.update {
                it.copy(
                    wkTemplate = it.wkTemplate.copy(
                        name = it.wkTemplate.name + " (${repository.getWorkoutTemplateCount() + 1})",
                        elements = repository.retrieveSavedWorkoutTemplateElements().take(Short.MAX_VALUE.toInt())
                    )
                )
            }
            addNewExercisesToTemplate(repository.getAllExercises(navArgs.addedExercises))
        }
    }

    private fun addNewExercisesToTemplate(exercises: List<Exercise>) {
        val elements = state.value.wkTemplate.elements.toMutableList()
        exercises.forEach { exercise ->
            elements.add(WkTemplateElement(exercise = exercise))
        }
        _state.update { it.copy(wkTemplate = it.wkTemplate.copy(elements = elements)) }
    }

    private fun changeWkDescription(newDescription: String) {
        if (newDescription != "") _state.update {
            it.copy(wkTemplate = it.wkTemplate.copy(description = newDescription))
        }
        else _state.update {
            it.copy(wkTemplate = it.wkTemplate.copy(description = null))
        }
    }


    private fun createWorkout() {
        ioLaunch {
            val updatedElements = state.value.wkTemplate.elements.mapIndexed { index, element ->
                element.copy(position = index)
            }

            _state.update { it.copy(wkTemplate = it.wkTemplate.copy(elements = updatedElements)) }

            when (val creation = repository.createWorkout(state.value.wkTemplate)) {
                is Result.Success -> sendEvent(EditWkEvent.NavigateToHome)
                is Result.Error -> _state.update { it.copy(error = creation.error) }
            }
        }

    }


    private fun navigateToHome() {
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

    private fun updateWkElementReps(newReps: Int) {
        if (newReps > Short.MAX_VALUE) {
            return
        }
        val index = state.value.selectedElementIndex
        if (index == null)
            return
        val updatedElements = state.value.wkTemplate.elements.toMutableList().apply {
            this[index] = this[index].copy(reps = newReps)
        }
        _state.update { it.copy(wkTemplate = state.value.wkTemplate.copy(elements = updatedElements)) }
    }

    private fun updateWkElementSets(newSets: Int) {
        if (newSets > Short.MAX_VALUE) {
            return
        }

        val index = state.value.selectedElementIndex
        if (index == null)
            return
        val updatedElements = state.value.wkTemplate.elements.toMutableList().apply {
            this[index] = this[index].copy(sets = newSets)
        }
        _state.update { it.copy(wkTemplate = state.value.wkTemplate.copy(elements = updatedElements)) }
    }

    private fun updateWkElementWeight(newWeight: Double) {
        if (newWeight > WEIGHT_MAX) {
            return
        }

        val index = state.value.selectedElementIndex
        if (index == null)
            return
        val updatedElements = state.value.wkTemplate.elements.toMutableList().apply {
            this[index] = this[index].copy(weight = newWeight)
        }
        _state.update { it.copy(wkTemplate = state.value.wkTemplate.copy(elements = updatedElements)) }
    }

    private fun updateWkElementRest(newRest: Int) {
        if (newRest > Short.MAX_VALUE) {
            return
        }

        val index = state.value.selectedElementIndex
        if (index == null)
            return
        val updatedElements = state.value.wkTemplate.elements.toMutableList().apply {
            this[index] = this[index].copy(rest = newRest)
        }
        _state.update { it.copy(wkTemplate = state.value.wkTemplate.copy(elements = updatedElements)) }
    }

    private fun deleteWkElement() {
        val index = state.value.selectedElementIndex
        if (index == null)
            return
        val updatedElements = state.value.wkTemplate.elements.toMutableList().apply {
            if (index in indices) {
                removeAt(index)
            }
        }
        _state.update {
            it.copy(
                wkTemplate = state.value.wkTemplate.copy(elements = updatedElements),
                selectedElementIndex = null
            )
        }
    }

}