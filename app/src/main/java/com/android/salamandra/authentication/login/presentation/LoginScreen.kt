package com.android.salamandra.authentication.login.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra.destinations.HomeScreenDestination
import com.android.salamandra.destinations.RegisterScreenDestination
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.presentation.components.MyCircularProgressbar
import com.android.salamandra._core.presentation.components.MyImageLogo
import com.android.salamandra.authentication.commons.presentation.textfields.MyAuthTextField
import com.android.salamandra.authentication.commons.presentation.textfields.MyPasswordTextField
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        when (events) {
            LoginEvent.NavigateToSignUp -> navigator.navigate(RegisterScreenDestination)
            LoginEvent.NavigateToHome -> navigator.navigate(HomeScreenDestination)
            null -> {}
        }
    }

    if (state.loading) {
        MyCircularProgressbar()
    } else {
        ScreenBody(
            state = state,
            sendIntent = viewModel::dispatch,
        )
    }
}

@Composable
private fun ScreenBody(
    state: LoginState,
    sendIntent: (LoginIntent) -> Unit,
) {
    val defaultPad = 8;
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(secondary),
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val fieldWeights: Map<String, Float> = mapOf(
                "top-pad" to 0.1f,
                "under-logo" to 0.1f,
                "username" to 0.5f,
                "password" to 0.1f,
                "between-fields" to 0.1f,
                "over-button" to 0.1f,
                "login" to 0.1f,
                "under-button" to 0.1f,
            )
            Spacer(modifier = Modifier.weight(fieldWeights["top-pad"]?: 1f))
            MyImageLogo()
            Spacer(modifier = Modifier.weight(fieldWeights["under-logo"]?: 1f))
            MyAuthTextField(
                modifier = Modifier
                    .weight(fieldWeights["username"]?: 1f)
                    .fillMaxWidth(),
                value = state.email,
                onValueChange = {
                    sendIntent(LoginIntent.ChangeEmail(it))
                },
                textResource = R.string.username_or_email
            )
            Spacer(modifier = Modifier.weight(fieldWeights["between-fields"]?: 1f))
            MyPasswordTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = defaultPad.dp),
                value = state.password,
                hint = stringResource(R.string.password),
                onValueChange = { sendIntent(LoginIntent.ChangePassword(it)) }
            )
            Text(
                text = stringResource(R.string.don_t_have_an_account_register),
                color = primaryVariant,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable { sendIntent(LoginIntent.GoToSignup) }
                    .align(Alignment.End)
                    .padding(vertical = defaultPad.dp)
            )

            OutlinedButton(
                onClick = { sendIntent(LoginIntent.Login) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = (3 * defaultPad).dp),
                border = BorderStroke(2.dp, primaryVariant),
                shape = RoundedCornerShape(40)
            ) {
                Text(
                    text = stringResource(R.string.login),
                    fontSize = 16.sp,
                    color = primaryVariant,
                    modifier = Modifier.padding(vertical = defaultPad.dp),
                )
            }
        }



        if (state.error != null)
            ErrorDialog(
                error = state.error.asUiText(),
                onDismiss = { sendIntent(LoginIntent.CloseError) }
            )


    }
}

@Preview
@Composable
fun LightPreview() {
    SalamandraTheme {
        ScreenBody(
            state = LoginState.initial,
            sendIntent = {},
        )
    }
}
