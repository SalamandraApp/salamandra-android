package com.android.salamandra.authentication.verifyAccount.presentation

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra.authentication.verifyAccount.domain.Repository
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.presentation.UiText
import com.android.salamandra.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class VerifyCodeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    ioDispatcher: CoroutineDispatcher,
    private val repository: Repository,
) : BaseViewModel<VerifyCodeState, VerifyCodeIntent, VerifyCodeEvent>(
    VerifyCodeState.initial,
    ioDispatcher
) {

    override fun reduce(intent: VerifyCodeIntent) {
        when (intent) {
            is VerifyCodeIntent.Error -> _state.update { it.copy(error = intent.error) }

            is VerifyCodeIntent.CloseError ->_state.update { it.copy(error = null) }

            is VerifyCodeIntent.ChangeCode -> _state.update { it.copy(code = intent.code) }

            is VerifyCodeIntent.ConfirmCode -> onVerifyCode()
        }
    }

    init {
        val navArgs: VerifyCodeNavArgs = savedStateHandle.navArgs()
        _state.update { it.copy(username = navArgs.username, email = navArgs.email, password = navArgs.password) }
    }

    private fun onVerifyCode() {
        ioLaunch {
            when (val confirmation =
                repository.confirmRegister(
                    username = state.value.username,
                    code = state.value.code,
                    email = state.value.email,
                    password = state.value.password
                )) {
                is Result.Error -> _state.update { it.copy(error = confirmation.error) }
                is Result.Success -> sendEvent(VerifyCodeEvent.NavigateToHome)
            }
        }
    }
}
