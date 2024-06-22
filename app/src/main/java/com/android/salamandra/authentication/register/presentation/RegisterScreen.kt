package com.android.salamandra.authentication.register.presentation

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra.destinations.LoginScreenDestination
import com.android.salamandra.destinations.VerifyCodeScreenDestination
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.presentation.components.MyCircularProgressbar
import com.android.salamandra._core.presentation.components.MyImageLogo
import com.android.salamandra.authentication.commons.presentation.textfields.MyAuthTextField
import com.android.salamandra.authentication.commons.presentation.textfields.MyPasswordTextField
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.salamandraColor
import com.android.salamandra.ui.theme.secondary
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
    val defaultPad = 8;
    val errorMessageHeight = 20.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(secondary),
    ) {
        var repeatPassword by remember { mutableStateOf("") }
        var isSamePassword by remember { mutableStateOf(true) }
        var isUsernameValid by remember { mutableStateOf(true) }

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MyImageLogo()

            // USERNAME
            MyAuthTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = (2 * defaultPad).dp, bottom = defaultPad.dp),
                value = state.username,
                onValueChange = {
                    sendIntent(RegisterIntent.ChangeUsername(it))
                    isUsernameValid = (state.username != "")
                },
                textResource = R.string.username
            )
            if (!isUsernameValid) {
                Row (
                    modifier = Modifier
                        .align(Alignment.Start)
                        .height(errorMessageHeight)
                        .padding(vertical = 1.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(errorMessageHeight - 3.dp)
                            .align(Alignment.CenterVertically),
                        imageVector = Icons.Outlined.ErrorOutline,
                        tint = colorError,
                        contentDescription = "Search workout"
                    )
                    Text(
                        text = stringResource(R.string.username_shouldn_t_be_empty),
                        color = colorError,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp),
                        style = NormalTypo,
                        fontSize = 13.sp
                    )
                }
            }

            // EMAIL
            MyAuthTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = defaultPad.dp, bottom = defaultPad.dp),
                value = state.email,
                onValueChange = {
                    sendIntent(RegisterIntent.ChangeEmail(it))
                },
                textResource = R.string.email
            )
            if (!state.isEmailValid) {
                Row (
                    modifier = Modifier
                        .align(Alignment.Start)
                        .height(errorMessageHeight)
                        .padding(vertical = 1.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(errorMessageHeight - 3.dp)
                            .align(Alignment.CenterVertically),
                        imageVector = Icons.Outlined.ErrorOutline,
                        tint = colorError,
                        contentDescription = "Search workout"
                    )
                    Text(
                        text = stringResource(R.string.email_must_have_a_valid_format),
                        color = colorError,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp),
                        style = NormalTypo,
                        fontSize = 13.sp
                    )
                }
            }
            MyPasswordTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = defaultPad.dp, bottom = defaultPad.dp),
                value = state.password,
                hint = stringResource(R.string.password),
                onValueChange = { sendIntent(RegisterIntent.ChangePassword(it)) }
            )
            if (state.passwordFormatError!= null) {
                Row (
                    modifier = Modifier
                        .align(Alignment.Start)
                        .height(errorMessageHeight)
                        .padding(vertical = 1.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(errorMessageHeight - 3.dp)
                            .align(Alignment.CenterVertically),
                        imageVector = Icons.Outlined.ErrorOutline,
                        tint = colorError,
                        contentDescription = "Search workout"
                    )
                    Text(
                        text = state.passwordFormatError.asString(),
                        color = colorError,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp),
                        style = NormalTypo,
                        fontSize = 13.sp
                    )
                }
            }

            MyPasswordTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = defaultPad.dp, bottom = defaultPad.dp),
                value = repeatPassword,
                hint = stringResource(R.string.repeat_password),
                onValueChange = {
                    repeatPassword = it
                    isSamePassword = repeatPassword == state.password
                }
            )
            if (!isSamePassword) {
                Row (
                    modifier = Modifier
                        .align(Alignment.Start)
                        .height(errorMessageHeight)
                        .padding(vertical = 1.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(errorMessageHeight - 3.dp)
                            .align(Alignment.CenterVertically),
                        imageVector = Icons.Outlined.ErrorOutline,
                        tint = colorError,
                        contentDescription = "Search workout"
                    )
                    Text(
                        text = stringResource(R.string.passwords_must_coincide),
                        color = colorError,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp),
                        style = NormalTypo,
                        fontSize = 13.sp
                    )
                }
            }

            Text(
                text = stringResource(R.string.already_have_an_account_log_in),
                modifier = Modifier
                    .clickable { sendIntent(RegisterIntent.GoToSignIn) }
                    .align(Alignment.End)
                    .padding(4.dp),
                fontSize = 14.sp,
                color = salamandraColor,
            )

            OutlinedButton(
                onClick = {
                    if (state.isEmailValid && state.passwordFormatError != null && isSamePassword && isUsernameValid) {
                        sendIntent(
                            RegisterIntent.OnRegister(
                                state.username,
                                state.email,
                                state.password
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = (3 * defaultPad).dp),
                border = BorderStroke(2.dp, primaryVariant),
                shape = RoundedCornerShape(40)
            ) {
                Text(
                    text = stringResource(R.string.register),
                    fontSize = 16.sp,
                    style = SemiTypo,
                    color = primaryVariant,
                    modifier = Modifier.padding(vertical = defaultPad.dp),
                )
            }
        }
        if (state.error != null)
            ErrorDialog(
                error = state.error.asUiText(),
                onDismiss = { sendIntent(RegisterIntent.CloseError) }
            )
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