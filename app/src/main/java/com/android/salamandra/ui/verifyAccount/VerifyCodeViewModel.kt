package com.android.salamandra.ui.verifyAccount

import com.android.salamandra.core.boilerplate.BaseViewModel
import com.android.salamandra.domain.Repository
import com.android.salamandra.domain.error.Result
import com.android.salamandra.ui.UiText
import com.android.salamandra.ui.register.RegisterIntent
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
        }
    }

    private fun onError(error: UiText) = _state.update { it.copy(error = error) }
    private fun onCloseError() = _state.update { it.copy(error = null) }
    private fun onLoading(isLoading: Boolean) = _state.update { it.copy(loading = isLoading) }

    private fun onVerifyCode() {
        ioLaunch {
            when (val confirmation =
                repository.confirmRegister(
                    username = state.value.username,
                    code = state.value.code
                )) {
                is Result.Error -> TODO()
                is Result.Success -> TODO()
            }
        }
    }
}