package com.android.salamandra._core.boilerplate.template

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.presentation.UiText
import com.android.salamandra.authentication.verifyAccount.presentation.VerifyCodeNavArgs
import com.android.salamandra.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class ViewModel @Inject constructor(ioDispatcher: CoroutineDispatcher, savedStateHandle: SavedStateHandle) :
    BaseViewModel<tState, tIntent, tEvent>(tState.initial, ioDispatcher) {

    override fun reduce(intent: tIntent) {
        when (intent) {
            is tIntent.Error -> _state.update { it.copy(error = intent.error) }
            is tIntent.CloseError -> _state.update { it.copy(error = null) }
        }
    }

    init {
        val navArgs: tNavArgs = savedStateHandle.navArgs()
    }

}