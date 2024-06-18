package com.android.salamandra.home.presentation

import androidx.lifecycle.viewModelScope
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.presentation.UiText
import com.android.salamandra.home.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: Repository,
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

            is HomeIntent.Logout -> onLogout()

            is HomeIntent.NewWk -> sendEvent(HomeEvent.NavigateToEditWk)

            is HomeIntent.BottomBarClicked -> sendEvent(HomeEvent.BottomBarClicked(intent.destination))
        }
    }

    private fun onError(error: RootError) = _state.update { it.copy(error = error) }

    private fun onCloseError() = _state.update { it.copy(error = null) }


    private fun onLoading(isLoading: Boolean) = _state.update { it.copy(loading = isLoading) }

    private fun onLogout() {
        ioLaunch { repository.logout() }
        sendEvent(HomeEvent.Logout)
    }
}


