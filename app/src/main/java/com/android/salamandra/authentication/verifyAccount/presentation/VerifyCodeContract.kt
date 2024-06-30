package com.android.salamandra.authentication.verifyAccount.presentation

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.presentation.UiText
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError


data class VerifyCodeState(
    val loading: Boolean,
    val error: RootError?,
    val username: String,
    val email: String,
    val code: String,
    val password: String,
) : State {
    companion object {
        val initial: VerifyCodeState = VerifyCodeState(
            loading = false,
            error = null,
            username = "",
            email = "",
            code = "",
            password = ""
        )
    }
}

sealed class VerifyCodeIntent: Intent {
    data class Loading(val isLoading: Boolean): VerifyCodeIntent()
    data class Error(val error: RootError): VerifyCodeIntent()
    data object CloseError: VerifyCodeIntent()
    data object ConfirmCode: VerifyCodeIntent()
    data class ChangeCode(val code: String): VerifyCodeIntent()
}

sealed class VerifyCodeEvent: Event {
    data object NavigateToHome: VerifyCodeEvent()
}

data class VerifyCodeNavArgs(
    val username: String,
    val email: String,
    val password: String
)
