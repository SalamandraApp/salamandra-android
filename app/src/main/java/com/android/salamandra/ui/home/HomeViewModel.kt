package com.android.salamandra.ui.home

import androidx.lifecycle.viewModelScope
import com.android.salamandra.domain.usecases.SearchExerciseUseCase
import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(val searchExerciseUseCase: SearchExerciseUseCase) :
    BaseViewModel<HomeState, HomeIntent>(HomeState.initial) {

    override fun reduce(
        state: HomeState,
        intent: HomeIntent
    ): HomeState { //This function reduces each intent with a when
        return when (intent) {
            is HomeIntent.Error -> state.copy(
                error = Error(isError = true, errorMsg = intent.errorMsg),
                loading = false
            )

            is HomeIntent.Loading -> state.copy(
                loading = true
            )

            is HomeIntent.SetExList -> state.copy(
                exList = intent.exList
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onSearchExercise(term: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { searchExerciseUseCase(term) }
            if(result != null){
                dispatch(HomeIntent.SetExList(result))
            }else{
                dispatch(HomeIntent.Error("Error while calling Search exercise API"))
            }
        }
    }


}