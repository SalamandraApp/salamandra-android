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

            is EditWkIntent.CloseError ->  _state.update { it.copy(error = null) }

            is EditWkIntent.ChangeWkName -> _state.update {
                it.copy(
                    wkTemplate = it.wkTemplate.copy(
                        name = intent.newName
                    )
                )
            }

            is EditWkIntent.ChangeWkDescription ->
                if (intent.newDescription != "") _state.update {
                    it.copy(wkTemplate = it.wkTemplate.copy(description = intent.newDescription))
                }
                else _state.update { it.copy(wkTemplate = it.wkTemplate.copy(description = null)) }

            is EditWkIntent.ShowSearchExercise -> _state.update { it.copy(showSearchExercise = intent.show) }

            is EditWkIntent.ChangeSearchTerm -> _state.update { it.copy(searchTerm = intent.newTerm) }

            EditWkIntent.SearchExercise -> searchExercise()

            is EditWkIntent.AddExerciseToTemplate -> addExerciseToTemplate(intent.exercise)

            EditWkIntent.NavigateUp -> sendEvent(EditWkEvent.NavigateUp)
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

    private fun addExerciseToTemplate(exercise: Exercise){
        val position = state.value.wkTemplate.elements.size + 1
        val wkTemplateElement = WkTemplateElement(exercise = exercise, position = position)
        _state.update { it.copy(wkTemplate = it.wkTemplate.copy(elements = it.wkTemplate.elements + wkTemplateElement)) }
    }
}