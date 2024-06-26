package com.android.salamandra.workouts.seeWk.presentation

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import com.android.salamandra.home.presentation.HomeEvent
import com.android.salamandra.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SeeWkViewModel @Inject constructor(ioDispatcher: CoroutineDispatcher, savedStateHandle: SavedStateHandle) :
    BaseViewModel<SeeWkState, SeeWkIntent, SeeWkEvent>(SeeWkState.initial, ioDispatcher) {

    override fun reduce(intent: SeeWkIntent) {
        when (intent) {
            is SeeWkIntent.Error -> _state.update { it.copy(error = intent.error) }
            is SeeWkIntent.CloseError -> _state.update { it.copy(error = null) }
            is SeeWkIntent.NavigateBack-> sendEvent(SeeWkEvent.NavigateBack)
        }
    }

    init {
        val navArgs: SeeWkNavArgs = savedStateHandle.navArgs()
        // TODO, check db with template id
        _state.update { it.copy(wkTemplate = WorkoutTemplate()) }
    }
}