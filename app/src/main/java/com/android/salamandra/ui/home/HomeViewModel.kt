package com.android.salamandra.ui.home

import com.vzkz.fitjournal.core.boilerplate.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(): BaseViewModel<HomeState, HomeIntent>(HomeState.initial) {

    override fun reduce(state: HomeState, intent: HomeIntent): HomeState { //This function reduces each intent with a when
        return when(intent){
            is HomeIntent.Error -> state.copy(
                error = Error(isError = true, errorMsg = intent.errorMsg),
                loading = false
            )

            is HomeIntent.Loading -> state.copy(
                loading = true
            )
        }
    }
    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onX(){ //Example fun
        dispatch(HomeIntent.Loading)
    }


}