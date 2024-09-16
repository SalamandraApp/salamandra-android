package com.android.salamandra.settings.presentation

import android.util.Log
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.CoreRepository
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.settings.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    ioDispatcher: CoroutineDispatcher,
    private val repository: Repository,
    private val coreRepository: CoreRepository
) :

    BaseViewModel<SettingsState, SettingsIntent, SettingsEvent>(SettingsState.initial, ioDispatcher) {
    override fun reduce(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.Error -> _state.update { it.copy(error = intent.error) }

            is SettingsIntent.CloseError -> _state.update { it.copy(error = null) }

            is SettingsIntent.NavigateUp -> sendEvent(SettingsEvent.NavigateUp)

            SettingsIntent.Logout -> onLogout()

            is SettingsIntent.ChangeAllCollapse -> {
                _state.value.let { currentState ->
                    val newState = currentState.updateAllSectionsCollapse(intent.state)
                    _state.value = newState
                }
            }

            is SettingsIntent.ChangeCollapse -> {
                _state.value.let { currentState ->
                    val newState = currentState.updateSectionCollapse(intent.section, collapse = intent.state)
                    _state.value = newState
                }
            }

            is SettingsIntent.ChangeSearchTerm -> _state.update { it.copy(searchTerm = intent.newTerm) }

            is SettingsIntent.SaveBirthday -> updateDateOfBirth(intent.newDateOfBirth)

            is SettingsIntent.SaveDisplayName -> updateDisplayName(intent.newDisplayName)
        }
    }

    init {
        ioLaunch {
            coreRepository.getUserDataAsFlow()?.collect { newUserData ->
                _state.update { it.copy(username = newUserData?.username, displayName = newUserData?.displayName, dateOfBirth = newUserData?.dateOfBirth) }
            }
        }
    }

    private fun updateDisplayName(newDisplayName: String?) {
           ioLaunch {
            when(val update = repository.changeDisplayName(newDisplayName)){
                is Result.Success -> {}
                is Result.Error -> _state.update { it.copy(error = update.error) }
            }
        }
    }

    private fun updateDateOfBirth(newDateOfBirth: LocalDate?) {
           ioLaunch {
            when(val update = repository.changeDateOfBirth(newDateOfBirth)){
                is Result.Success -> {}
                is Result.Error -> _state.update { it.copy(error = update.error) }
            }
        }
    }

    private fun onLogout(){
        Log.i("SETTINGS", "LOGGED OUT")
        ioLaunch {
            when(val logout = repository.logout()){
                is Result.Success -> sendEvent(SettingsEvent.NavigateToHome)
                is Result.Error -> _state.update { it.copy(error = logout.error) }
            }
        }

    }
}