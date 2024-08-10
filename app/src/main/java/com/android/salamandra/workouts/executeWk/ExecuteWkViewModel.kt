package com.android.salamandra.workouts.executeWk

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class ExecuteWkViewModel @Inject constructor(ioDispatcher: CoroutineDispatcher, savedStateHandle: SavedStateHandle) :
    BaseViewModel<ExecuteWkState, ExecuteWkIntent, ExecuteWkEvent>(ExecuteWkState.initial, ioDispatcher) {

    override fun reduce(intent: ExecuteWkIntent) {
        when (intent) {
            is ExecuteWkIntent.Error -> _state.update { it.copy(error = intent.error) }

            is ExecuteWkIntent.CloseError -> _state.update { it.copy(error = null) }
        }
    }

    init {
        val navArgs: ExecuteWkNavArgs = savedStateHandle.navArgs()
    }

}