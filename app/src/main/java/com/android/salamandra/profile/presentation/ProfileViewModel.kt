package com.android.salamandra.profile.presentation

import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.CoreRepository
import com.android.salamandra._core.domain.USER_WEIGHT_MIN
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.enums.FitnessGoal
import com.android.salamandra._core.domain.model.enums.FitnessLevel
import com.android.salamandra.profile.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    ioDispatcher: CoroutineDispatcher,
    private val repository: Repository,
    private val coreRepository: CoreRepository
) : BaseViewModel<ProfileState, ProfileIntent, ProfileEvent>(ProfileState.initial, ioDispatcher) {

    private companion object Constants {
        const val GOAL = "goal"
        const val LEVEL = "level"
    }

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

            is ProfileIntent.OpenEditFitness -> openEditFitness(intent.parameter)

            is ProfileIntent.EditFitness -> editFitness(
                intent.parameter,
                intent.newLevel,
                intent.newGoal
            )

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

                    is Result.Error -> _state.update {
                        it.copy(
                            error = userData.error,
                            loading = false
                        )
                    }
                }

                coreRepository.getUserDataAsFlow()?.collect { newUserData ->
                    _state.update { it.copy(userData = newUserData) }
                }
            } else _state.update { it.copy(loading = false) }
        }

    }

    private fun updateWeight() {
        if (state.value.userData == null)
            return

        ioLaunch {
            when(val update = repository.changeWeight(state.value.newWeight)){
                is Result.Success -> {}
                is Result.Error -> _state.update { it.copy(error = update.error) }
            }
            _state.update {
                it.copy(
                    editWeight = false,
                    newWeight = null,
                )
            }

        }
    }

    private fun updateFitness() {
        if (state.value.userData == null)
            return
        ioLaunch {
            if (state.value.editFitness == GOAL) {
                repository.changeFitnessGoal(state.value.newFitnessGoal)
                _state.update {
                    it.copy(
                        editFitness = "",
                        newFitnessGoal = null,
                    )
                }
            } else if (state.value.editFitness == LEVEL) {
                repository.changeFitnessLevel(state.value.newFitnessLevel)
                _state.update {
                    it.copy(
                        editFitness = "",
                        newFitnessLevel = null,
                    )
                }
            }
        }
    }

    private fun openEditFitness(parameter: String) {
        if (parameter == LEVEL) {
            _state.update {
                it.copy(
                    editFitness = parameter,
                    newFitnessLevel = state.value.userData?.fitnessLevel
                        ?: FitnessLevel.getLowest()
                )
            }
        } else if (parameter == GOAL) {
            _state.update {
                it.copy(
                    editFitness = parameter,
                    newFitnessGoal = state.value.userData?.fitnessGoal
                        ?: FitnessGoal.getLowest()
                )
            }
        }
    }

    private fun editFitness(parameter: String, newLevel: FitnessLevel?, newGoal: FitnessGoal?) {
        if (parameter == LEVEL && newLevel != null) {
            _state.update { it.copy(newFitnessLevel = newLevel) }
        } else if (parameter == GOAL && newGoal != null) {
            _state.update { it.copy(newFitnessGoal = newGoal) }
        }
    }

}