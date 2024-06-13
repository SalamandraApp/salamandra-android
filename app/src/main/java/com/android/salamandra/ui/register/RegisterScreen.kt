package com.android.salamandra.ui.register

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra.destinations.LoginScreenDestination
import com.android.salamandra.destinations.VerifyCodeScreenDestination
import com.android.salamandra.ui.components.ErrorDialog
import com.android.salamandra.ui.components.MyCircularProgressbar
import com.android.salamandra.ui.components.MyColumn
import com.android.salamandra.ui.components.MyImageLogo
import com.android.salamandra.ui.components.MySpacer
import com.android.salamandra.ui.components.textFields.MyEmailTextField
import com.android.salamandra.ui.components.textFields.MyPasswordTextField
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.salamandraColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun RegisterScreen(
    navigator: DestinationsNavigator,
    viewModel: RegisterViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        when (events) {
            RegisterEvent.NavigateToLogin ->  navigator.navigate(LoginScreenDestination)
            RegisterEvent.NavigateToVerifyCode ->  navigator.navigate(VerifyCodeScreenDestination(state.username))
            null -> {}
        }
    }

    if (state.loading) {
        MyCircularProgressbar()
    } else{
        ScreenBody(
            state = state,
            sendIntent = viewModel::dispatch,
        )
    }
}

@Composable
private fun ScreenBody(
    state: RegisterState,
    sendIntent: (RegisterIntent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        var repeatPassword by remember { mutableStateOf("") }
        var isSamePassword by remember { mutableStateOf(true) }
        var isNicknameValid by remember { mutableStateOf(true) }

        MyColumn {
            MyImageLogo()
            MyEmailTextField(modifier = Modifier, value = state.email, onValueChange = {
                sendIntent(RegisterIntent.ChangeEmail(it))
            })
            if (!state.isEmailValid) {
                Text(
                    text = stringResource(R.string.email_must_have_a_valid_format),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(2.dp),
                )
            } else MySpacer(size = 8)

            MyPasswordTextField(
                value = state.password,
                hint = stringResource(R.string.password),
                onValueChange = { sendIntent(RegisterIntent.ChangePassword(it)) }
            )
            if (state.passwordFormatError != null) {
                Text(
                    text = state.passwordFormatError.asString(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(2.dp)
                )
            } else MySpacer(size = 8)

            MyPasswordTextField(
                value = repeatPassword,
                hint = stringResource(R.string.repeat_password),
                onValueChange = {
                    repeatPassword = it
                    isSamePassword = repeatPassword == state.password
                }
            )
            if (!isSamePassword) {
                Text(
                    text = stringResource(R.string.passwords_must_coincide),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(2.dp)
                )
            } else MySpacer(size = 8)

            OutlinedTextField(
                modifier = Modifier,
                value = state.username,
                label = {
                    Text(
                        text = stringResource(R.string.user_name),
                        fontSize = 16.sp,
                    )
                },
                onValueChange = {
                    sendIntent(RegisterIntent.ChangeUsername(it))
                    isNicknameValid = (state.username != "")
                }
            )
            if (!isNicknameValid) {
                Text(
                    text = stringResource(R.string.nickname_shouldn_t_be_empty),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(2.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = stringResource(R.string.login),
                modifier = Modifier
                    .clickable { sendIntent(RegisterIntent.GoToSignIn) }
                    .align(Alignment.End)
                    .padding(4.dp),
                fontSize = 14.sp,
                color = salamandraColor,
            )
        }
        OutlinedButton(
            onClick = {
                if (state.isEmailValid && state.passwordFormatError != null && isSamePassword && isNicknameValid) {
                    sendIntent(RegisterIntent.OnRegister(state.username, state.email, state.password))
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
                .padding(bottom = 12.dp)
                .fillMaxWidth(),
            border = BorderStroke(1.dp, salamandraColor),
            shape = RoundedCornerShape(42)
        ) {
            Text(
                text = stringResource(R.string.register),
                fontSize = 16.sp,
                color = salamandraColor,
                modifier = Modifier.padding(4.dp)
            )
        }

        if (state.error != null) ErrorDialog(error = state.error)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScreenPreview() {
    SalamandraTheme {
        ScreenBody(
            state = RegisterState.initial,
            sendIntent = {},
        )
    }
}