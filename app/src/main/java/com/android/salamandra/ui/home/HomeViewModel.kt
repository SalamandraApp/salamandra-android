package com.android.salamandra.ui.home

import androidx.lifecycle.viewModelScope
import com.android.salamandra.core.boilerplate.BaseViewModel
import com.android.salamandra.domain.Repository
import com.android.salamandra.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: Repository
) :
    BaseViewModel<HomeState, HomeIntent>(HomeState.initial) {

    override fun reduce(
        intent: HomeIntent
    ){ //This function reduces each intent with a when
        when (intent) {
            is HomeIntent.Error -> onError(intent.error)
            is HomeIntent.CloseError -> onCloseError()
            is HomeIntent.Loading -> onLoading(intent.isLoading)
            is HomeIntent.SearchExercise -> onSearchExercise(intent.term)
        }
    }


    private fun onSearchExercise(term: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getExercise(term)
            state = if (result != null) {
                state.copy(exList = result)
            } else {
                state.copy(error = null) // TODO should be done using Error handling
            }
        }
    }
    private fun onError(error: UiText) {
        state = state.copy(error = error)
    }

    private fun onCloseError() {
        state = state.copy(error = null)
    }
    private fun onLoading(isLoading: Boolean) {
        state = state.copy(loading = isLoading)
    }

    fun onLogout() {
        viewModelScope.launch(Dispatchers.IO) { repository.logout()}
    }
}


