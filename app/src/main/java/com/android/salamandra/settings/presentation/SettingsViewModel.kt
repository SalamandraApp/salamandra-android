package com.android.salamandra.settings.presentation

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(ioDispatcher: CoroutineDispatcher, savedStateHandle: SavedStateHandle) :
    BaseViewModel<SettingsState, SettingsIntent, SettingsEvent>(SettingsState.initial, ioDispatcher) {

    override fun reduce(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.Error -> _state.update { it.copy(error = intent.error) }
            is SettingsIntent.CloseError -> _state.update { it.copy(error = null) }
            is SettingsIntent.NavigateUp -> sendEvent(SettingsEvent.NavigateUp)
        }
    }

    init {
        val navArgs: SettingsNavArgs = savedStateHandle.navArgs()
    }

}