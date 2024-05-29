package com.android.salamandra.core.boilerplate.template

import com.android.salamandra.core.boilerplate.BaseViewModel
import com.android.salamandra.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class tViewModel @Inject constructor() : BaseViewModel<tState, tIntent>(tState.initial) {

    override fun reduce(intent: tIntent) {
        when (intent) {
            is tIntent.Error -> onError(intent.error)
            is tIntent.CloseError -> onCloseError()
            is tIntent.Loading -> onLoading(intent.isLoading)
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
}