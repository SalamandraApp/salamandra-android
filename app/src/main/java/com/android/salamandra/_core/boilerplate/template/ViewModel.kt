package com.android.salamandra._core.boilerplate.template

import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.error.RootError
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
        }
    }
    private fun onError(error: RootError) = _state.update { it.copy(error = error) }
    private fun onCloseError() = _state.update { it.copy(error = null) }
}