package com.android.salamandra.home.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.CoreRepository
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.presentation.UiText
import com.android.salamandra.home.domain.Repository
import com.android.salamandra.splash.presentation.SplashEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val coreRepository: CoreRepository,
    ioDispatcher: CoroutineDispatcher
) :
    BaseViewModel<HomeState, HomeIntent, HomeEvent>(HomeState.initial, ioDispatcher) {

    override fun reduce(
        intent: HomeIntent
    ) { //This function reduces each intent with a when
        when (intent) {
            is HomeIntent.Error -> onError(intent.error)

            is HomeIntent.CloseError -> onCloseError()

            is HomeIntent.Loading -> onLoading(intent.isLoading)

            is HomeIntent.NewWk -> sendEvent(HomeEvent.NavigateToEditWk)

            is HomeIntent.SeeWk -> sendEvent(HomeEvent.NavigateToSeeWk(intent.wkTemplateId))

            is HomeIntent.BottomBarClicked -> sendEvent(HomeEvent.BottomBarClicked(intent.destination))
        }
    }

    init {
        ioLaunch {
            if (coreRepository.isUserLogged() && repository.isLocalDbEmpty())
                repository.getWkPreviewsFromRemoteAndStoreInLocal()

            repository.getWkPreviews().collect{ newList ->
                _state.update { it.copy(wkPreviewList = newList) }
            }
        }
    }

    private fun onError(error: RootError) = _state.update { it.copy(error = error) }

    private fun onCloseError() = _state.update { it.copy(error = null) }

    private fun onLoading(isLoading: Boolean) = _state.update { it.copy(loading = isLoading) }


}


