package com.android.salamandra._core.boilerplate.template

import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.presentation.UiText
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError


data class tState(
    val error: RootError?
) : State {
    companion object {
        val initial: tState = tState(
            error = null
        )
    }
}

sealed class tIntent: Intent {
    data class Error(val error: RootError): tIntent()
    data object CloseError: tIntent()
}

sealed class tEvent: Event{
}

data class tNavArgs(
    val dummy: Int? = null
): NavArgs