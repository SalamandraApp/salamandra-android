package com.android.salamandra.profile.presentation

import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.CoreRepository
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.error.RootError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    ioDispatcher: CoroutineDispatcher,
    private val coreRepository: CoreRepository
) : BaseViewModel<ProfileState, ProfileIntent, ProfileEvent>(ProfileState.initial, ioDispatcher) {

    override fun reduce(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.Error -> _state.update { it.copy(error = intent.error) }

            is ProfileIntent.CloseError -> _state.update { it.copy(error = null) }

            is ProfileIntent.BottomBarClicked -> sendEvent(ProfileEvent.BottomBarClicked(intent.destination))

            ProfileIntent.GoToLogin -> sendEvent(ProfileEvent.NavigateToLogin)

            ProfileIntent.GoToSettings -> sendEvent(ProfileEvent.NavigateToSettings)
        }
    }

    init {
        ioLaunch {
            if (coreRepository.isUserLogged()) {
                when (val userData = coreRepository.getUserData()) {
                    is Result.Success -> _state.update {
                        it.copy(
                            isSignedIn = true,
                            userData = userData.data
                        )
                    }

                    is Result.Error -> _state.update { it.copy(error = userData.error) }
                }
            }
        }

    }

}