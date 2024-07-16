package com.android.salamandra.workouts.searchExercise.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra.workouts.searchExercise.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SearchExerciseViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: Repository
) : BaseViewModel<SearchExerciseState, SearchExerciseIntent, SearchExerciseEvent>(
    SearchExerciseState.initial, ioDispatcher
) {

    override fun reduce(intent: SearchExerciseIntent) {
        when (intent) {
            is SearchExerciseIntent.Error -> _state.update { it.copy(error = intent.error) }

            is SearchExerciseIntent.CloseError -> _state.update { it.copy(error = null) }

            is SearchExerciseIntent.ChangeSearchTerm -> onChangeSearchTerm(intent.newTerm)

            SearchExerciseIntent.NavigateToEdit -> sendEvent(SearchExerciseEvent.NavigateToEdit)

            SearchExerciseIntent.SearchExercise -> searchExercise()

            is SearchExerciseIntent.AddExercise -> addExerciseToTemplate(intent.exercise)
        }
    }

    private fun searchExercise() {
        viewModelScope.launch {
            when (val search =
                withContext(ioDispatcher) { repository.getExercises(state.value.searchTerm) }) {
                is Result.Success -> _state.update { it.copy(searchResultExercises = search.data) }
                is Result.Error -> _state.update { it.copy(error = search.error) }
            }
        }

    }

    private fun onChangeSearchTerm(newTerm: String) {
        _state.update { it.copy(searchTerm = newTerm) }
        Log.i("SLM", "Dummy")
    }

    private fun addExerciseToTemplate(exercise: String){
        _state.update { it.copy(addedExercisesIds = it.addedExercisesIds + exercise) }
    }
}