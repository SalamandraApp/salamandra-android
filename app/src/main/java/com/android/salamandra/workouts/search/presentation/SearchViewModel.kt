package com.android.salamandra.workouts.search.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra.navArgs
import com.android.salamandra.workouts.search.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: Repository
) :
    BaseViewModel<SearchState, SearchIntent, SearchEvent>(SearchState.initial, ioDispatcher) {

    override fun reduce(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.Error -> _state.update { it.copy(error = intent.error) }
            is SearchIntent.CloseError -> _state.update { it.copy(error = null) }
            is SearchIntent.AddExercise -> addExerciseToTemplate(intent.exercise)
            is SearchIntent.ChangeSearchTerm -> _state.update { it.copy(searchTerm = intent.newTerm) }
            SearchIntent.SearchExercise ->  searchExercise()
            SearchIntent.NavigateToEdit -> sendEvent(SearchEvent.NavigateToEdit)
            SearchIntent.HideBottomSheet -> _state.update { it.copy(selectedExercise = null) }
            is SearchIntent.ShowBottomSheet -> _state.update { it.copy(selectedExercise = intent.exercise) }
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

    private fun addExerciseToTemplate(exercise: Exercise) {
        ioLaunch {
            when(val insertion = repository.insertExerciseInLocal(exercise = exercise)){
                is Result.Success -> {}
                is Result.Error -> _state.update { it.copy(error = insertion.error) }
            }
        }
        _state.update { it.copy(addedExercisesIds = it.addedExercisesIds + exercise.exId) }
    }

}