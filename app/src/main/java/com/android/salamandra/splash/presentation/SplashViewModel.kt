package com.android.salamandra.splash.presentation

import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.CoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.android.salamandra.splash.domain.Repository


@HiltViewModel
class SplashViewModel @Inject constructor(
    ioDispatcher: CoroutineDispatcher,
    private val coreRepository: CoreRepository,
    private val featureRepository: Repository
) : BaseViewModel<SplashState, SplashIntent, SplashEvent>(SplashState.initial, ioDispatcher) {

    override fun reduce(intent: SplashIntent) { //This function reduces each intent with a when
        when (intent) {
            is SplashIntent.Error -> _state.update { it.copy(error = intent.error) }

            is SplashIntent.Loading -> _state.update { it.copy(loading = true) }
        }
    }

    init {
        ioLaunch {
            if (!coreRepository.isUserLogged() || !featureRepository.isLocalDbEmpty())
                sendEvent(SplashEvent.NavigateToHome) //User not logged in or database with data
            else{
                featureRepository.getWkPreviewsFromRemote()
            }

        }

    }
}