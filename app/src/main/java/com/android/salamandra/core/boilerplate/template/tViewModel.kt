package com.vzkz.profinder.core.boilerplate.template

import com.android.salamandra.core.boilerplate.template.tIntent
import com.android.salamandra.core.boilerplate.template.tState
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.model.UiError
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class tViewModel @Inject constructor(): BaseViewModel<tState, tIntent>(tState.initial) {

    override fun reduce(state: tState, intent: tIntent): tState {
        return when(intent){
            is tIntent.Loading -> TODO()


            is tIntent.Error -> state.copy(
                error = UiError(true, intent.errorMsg),
                loading = false
            )

            tIntent.CloseError -> state.copy(
                error = UiError(false, null),
                loading = false
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onX(){ //Example fun
        dispatch(tIntent.Loading(true))
    }

    fun onCloseDialog() = dispatch(tIntent.CloseError)

}