package com.android.salamandra.authentication.verifyAccount.presentation

import com.android.salamandra.authentication.verifyAccount.domain.Repository
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.error.RootError
import com.android.salamandra._core.presentation.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class VerifyCodeViewModel @Inject constructor(
    ioDispatcher: CoroutineDispatcher,
    private val repository: Repository,
) : BaseViewModel<VerifyCodeState, VerifyCodeIntent, VerifyCodeEvent>(VerifyCodeState.initial, ioDispatcher) {

    override fun reduce(intent: VerifyCodeIntent) {
        when (intent) {
            is VerifyCodeIntent.Error -> onError(intent.error)

            is VerifyCodeIntent.CloseError -> onCloseError()

            is VerifyCodeIntent.Loading -> onLoading(intent.isLoading)

            is VerifyCodeIntent.ChangeCode -> _state.update { it.copy(code = intent.code) }

            is VerifyCodeIntent.ConfirmCode -> onVerifyCode()
            is VerifyCodeIntent.SetUsername -> _state.update { it.copy(username = intent.username) }
        }
    }

    private fun onError(error: RootError) = _state.update { it.copy(error = error) }
    private fun onCloseError() = _state.update { it.copy(error = null) }
    private fun onLoading(isLoading: Boolean) = _state.update { it.copy(loading = isLoading) }

    private fun onVerifyCode() {
        ioLaunch {
            when (val confirmation =
                repository.confirmRegister(
                    username = state.value.username,
                    code = state.value.code
                )) {
                is Result.Error -> _state.update { it.copy(error = confirmation.error) }
                is Result.Success -> sendEvent(VerifyCodeEvent.NavigateToHome)
            }
        }
    }
}