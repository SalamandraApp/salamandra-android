package com.android.salamandra.ui.verifyAccount

import com.android.salamandra.core.boilerplate.Event
import com.android.salamandra.ui.UiText
import com.android.salamandra.core.boilerplate.Intent
import com.android.salamandra.core.boilerplate.State


data class VerifyCodeState(
    val loading: Boolean,
    val error: UiText?,
    val username: String,
    val code: String,
) : State {
    companion object {
        val initial: VerifyCodeState = VerifyCodeState(
            loading = false,
            error = null,
            username = "",
            code = ""
        )
    }
}

sealed class VerifyCodeIntent: Intent {
    data class Loading(val isLoading: Boolean): VerifyCodeIntent()
    data class Error(val error: UiText): VerifyCodeIntent()
    data object CloseError: VerifyCodeIntent()
    data object ConfirmCode: VerifyCodeIntent()
    data class ChangeCode(val code: String): VerifyCodeIntent()
}

sealed class VerifyCodeEvent: Event {
}