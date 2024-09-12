package com.android.salamandra.profile.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.domain.model.User
import com.android.salamandra._core.domain.model.enums.FitnessGoal
import com.android.salamandra._core.domain.model.enums.FitnessLevel
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec


data class ProfileState(
    val loading: Boolean,
    val error: RootError?,
    val userData: User?,
    val isSignedIn: Boolean,

    val newWeight: Double? = null,
    val editWeight: Boolean = false,
    val editFitness: String = "",
    val newFitnessGoal: FitnessGoal? = null,
    val newFitnessLevel: FitnessLevel? = null,
) : State {
    companion object {
        val initial: ProfileState = ProfileState(
            loading = true,
            error = null,
            userData = null,
            isSignedIn = false
        )
    }
}

sealed class ProfileIntent: Intent {
    data class Error(val error: RootError): ProfileIntent()
    data object CloseError: ProfileIntent()

    data class EditWeight(val newWeight: Double): ProfileIntent()
    data object SaveNewWeight: ProfileIntent()
    data object OpenEditWeight: ProfileIntent()
    data class EditFitness(val parameter: String, val newLevel: FitnessLevel?, val newGoal: FitnessGoal?): ProfileIntent()
    data class OpenEditFitness(val parameter: String): ProfileIntent()
    data object SaveFitness: ProfileIntent()

    data class BottomBarClicked(val destination: DirectionDestinationSpec): ProfileIntent()
    data object GoToLogin: ProfileIntent()
    data object GoToSettings: ProfileIntent()
}

sealed class ProfileEvent: Event{
    data class BottomBarClicked(val destination: DirectionDestinationSpec): ProfileEvent()
    data object NavigateToLogin: ProfileEvent()
    data object NavigateToSettings: ProfileEvent()
}