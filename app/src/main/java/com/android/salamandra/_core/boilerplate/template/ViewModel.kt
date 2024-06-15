package com.android.salamandra._core.boilerplate.template

import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.presentation.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class ViewModel @Inject constructor(ioDispatcher: CoroutineDispatcher) : BaseViewModel<tState, tIntent, tEvent>(tState.initial, ioDispatcher) {

    override fun reduce(intent: tIntent) {
        when (intent) {
            is tIntent.Error -> onError(intent.error)
            is tIntent.CloseError -> onCloseError()
            is tIntent.Loading -> onLoading(intent.isLoading)
        }
    }
    private fun onError(error: UiText) = _state.update { it.copy(error = error) }
    private fun onCloseError() = _state.update { it.copy(error = null) }
    private fun onLoading(isLoading: Boolean) = _state.update { it.copy(loading = isLoading) }
}