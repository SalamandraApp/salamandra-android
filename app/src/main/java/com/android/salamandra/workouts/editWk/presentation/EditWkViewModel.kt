package com.android.salamandra.workouts.editWk.presentation

import androidx.lifecycle.viewModelScope
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.presentation.UiText
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra.workouts.editWk.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class EditWkViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: Repository
) :
    BaseViewModel<EditWkState, EditWkIntent, EditWkEvent>(EditWkState.initial, ioDispatcher) {

    override fun reduce(intent: EditWkIntent) {
        when (intent) {
            is EditWkIntent.Error -> _state.update { it.copy(error = intent.error) }

            is EditWkIntent.CloseError -> _state.update { it.copy(error = null) }

            EditWkIntent.NavigateUp -> sendEvent(EditWkEvent.NavigateUp)

            // Toggle bottom sheet
            is EditWkIntent.HideBottomSheet -> _state.update { it.copy(bottomSheet = false) }

            is EditWkIntent.ShowBottomSheet -> _state.update { it.copy(bottomSheet = true, exerciseSelectedIndex = intent.index) }

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

            is EditWkIntent.ChangeSearchTerm -> _state.update { it.copy(searchTerm = intent.newTerm) }

            is EditWkIntent.SearchExercise -> searchExercise()

            is EditWkIntent.AddExerciseToTemplate -> addExerciseToTemplate(intent.exercise)

            is EditWkIntent.NavigateToEdit -> sendEvent(EditWkEvent.NavigateToEdit)

            is EditWkIntent.NavigateToSearch -> sendEvent(EditWkEvent.NavigateToSearch)
        }
    }

    private fun searchExercise() {
        viewModelScope.launch {
            when (val search = withContext(ioDispatcher) { repository.getExercises(state.value.searchTerm) }){
                is Result.Success -> _state.update { it.copy(exerciseList = search.data) }
                is Result.Error -> _state.update { it.copy(error = search.error) }
            }
        }

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

    private fun addExerciseToTemplate(exercise: Exercise){
        val position = state.value.wkTemplate.elements.size + 1
        val wkTemplateElement = WkTemplateElement(exercise = exercise, position = position)
        _state.update { it.copy(wkTemplate = it.wkTemplate.copy(elements = it.wkTemplate.elements + wkTemplateElement)) }
    }
}