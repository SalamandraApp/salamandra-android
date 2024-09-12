package com.android.salamandra.profile.presentation

import android.util.Log
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.CoreRepository
import com.android.salamandra._core.domain.USER_WEIGHT_MIN
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.enums.FitnessGoal
import com.android.salamandra._core.domain.model.enums.FitnessLevel
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

            is ProfileIntent.EditWeight -> _state.update { it.copy(newWeight = intent.newWeight) }
            ProfileIntent.SaveNewWeight -> updateWeight()
            ProfileIntent.OpenEditWeight -> {
                val editableWeight = state.value.userData?.weight ?: USER_WEIGHT_MIN
                _state.update { it.copy(editWeight = true, newWeight = editableWeight) }
            }
            is ProfileIntent.OpenEditFitness ->  openEditFitness(intent.parameter)
            is ProfileIntent.EditFitness -> editFitness(intent.parameter, intent.newLevel, intent.newGoal)
            ProfileIntent.SaveFitness -> updateFitness()

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
                            userData = userData.data,
                            loading = false
                        )
                    }

                    is Result.Error -> _state.update { it.copy(error = userData.error, loading = false) }
                }
            } else _state.update { it.copy(loading = false) }
        }

    }

    private fun updateWeight() {
        // TODO
        // - Save locally
        // - Send to server
        if (state.value.userData == null)
            return
        _state.update { it.copy(editWeight = false, newWeight = null, userData = state.value.userData!!.copy(weight = state.value.newWeight)) }
    }

    private fun updateFitness () {
        // TODO
        // - Save locally
        // - Send to server
        if (state.value.userData == null)
            return
        if (state.value.editFitness == "goal") {
            _state.update { it.copy(editFitness = "", newFitnessGoal = null, userData = state.value.userData!!.copy(fitnessGoal = state.value.newFitnessGoal)) }
        }
        else if (state.value.editFitness == "level") {
            _state.update { it.copy(editFitness = "", newFitnessLevel= null, userData = state.value.userData!!.copy(fitnessLevel = state.value.newFitnessLevel)) }
        }
    }
    private fun openEditFitness(parameter: String) {
        if (parameter == "level") {
            _state.update {  it.copy(
                editFitness = parameter,
                newFitnessLevel = state.value.userData?.fitnessLevel
                    ?: FitnessLevel.getLowest()
            )}
        } else if (parameter == "goal") {
            _state.update {  it.copy(
                editFitness = parameter,
                newFitnessGoal = state.value.userData?.fitnessGoal
                    ?: FitnessGoal.getLowest()
            )}
        }
    }
    private fun editFitness(parameter: String, newLevel: FitnessLevel?, newGoal: FitnessGoal?) {
        if (parameter == "level" && newLevel != null) {
            _state.update { it.copy(newFitnessLevel = newLevel) }
        } else if (parameter == "goal" && newGoal != null) {
            _state.update { it.copy(newFitnessGoal = newGoal) }
        }
    }

}