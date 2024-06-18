package com.android.salamandra.profile.presentation

import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra.home.presentation.HomeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(ioDispatcher: CoroutineDispatcher) : BaseViewModel<ProfileState, ProfileIntent, ProfileEvent>(ProfileState.initial, ioDispatcher) {

    override fun reduce(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.Error -> onError(intent.error)
            is ProfileIntent.CloseError -> onCloseError()
            is ProfileIntent.BottomBarClicked -> sendEvent(ProfileEvent.BottomBarClicked(intent.destination))
        }
    }
    private fun onError(error: RootError) = _state.update { it.copy(error = error) }
    private fun onCloseError() = _state.update { it.copy(error = null) }
}