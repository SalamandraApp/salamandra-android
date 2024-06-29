package com.android.salamandra.authentication.register.presentation

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.presentation.components.MyCircularProgressbar
import com.android.salamandra._core.presentation.components.MyImageLogo
import com.android.salamandra.authentication.commons.presentation.textfields.MyAuthTextField
import com.android.salamandra.authentication.commons.presentation.textfields.MyPasswordTextField
import com.android.salamandra.authentication.login.presentation.LoginIntent
import com.android.salamandra.destinations.LoginScreenDestination
import com.android.salamandra.destinations.VerifyCodeScreenDestination
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.salamandraColor
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.tertiary
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
            RegisterEvent.NavigateToLogin -> navigator.navigate(LoginScreenDestination)
            RegisterEvent.NavigateToVerifyCode -> navigator.navigate(
                VerifyCodeScreenDestination(
                    state.username
                )
            )

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
    state: RegisterState,
    sendIntent: (RegisterIntent) -> Unit,
) {
    val defaultPad = 8;
    val errorMessageHeight = 20.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(tertiary),
    ) {
        var repeatPassword by remember { mutableStateOf("") }
        var isSamePassword by remember { mutableStateOf(true) }
        var isUsernameValid by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val nFields = 4
            val textFieldWeight = 100f
            val buttonWeight = 100f
            val betweenFieldsWeight = 50f
            val middlePadWeight = 50f

            val textPad = 12.dp

            val verticalPadWeight = (1000f -
                    nFields * textFieldWeight -
                    2 * middlePadWeight -
                    (nFields - 1) * betweenFieldsWeight -
                    buttonWeight)/2


            Spacer(modifier = Modifier.weight(verticalPadWeight))
            MyImageLogo()
            Spacer(modifier = Modifier.weight(middlePadWeight))

            // USERNAME
            MyAuthTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(textFieldWeight),
                value = state.username,
                onValueChange = {
                    sendIntent(RegisterIntent.ChangeUsername(it))
                    isUsernameValid = (it != "")
                },
                textResource = R.string.username
            )
            if (!isUsernameValid) {
                Row(
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
                        contentDescription = "username error icon"
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

            Spacer(modifier = Modifier.weight(betweenFieldsWeight))
            // EMAIL
            MyAuthTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(textFieldWeight),
                value = state.email,
                onValueChange = {
                    sendIntent(RegisterIntent.ChangeEmail(it))
                },
                textResource = R.string.email
            )
            if (!state.isEmailValid) {
                Row(
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
                        contentDescription = "Email error icon"
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
            Spacer(modifier = Modifier.weight(betweenFieldsWeight))
            MyPasswordTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(textFieldWeight),
                value = state.password,
                hint = stringResource(R.string.password),
                onValueChange = { sendIntent(RegisterIntent.ChangePassword(it)) }
            )
            if (state.passwordFormatError != null) {
                Row(
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
                        contentDescription = "Password format error icon"
                    )
                    Text(
                        text = state.passwordFormatError.asUiText().asString(),
                        color = colorError,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp),
                        style = NormalTypo,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(betweenFieldsWeight))
            MyPasswordTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(textFieldWeight),
                value = repeatPassword,
                hint = stringResource(R.string.repeat_password),
                onValueChange = {
                    repeatPassword = it
                }
            )
            LaunchedEffect(key1 = state.password) {
                isSamePassword = state.password == repeatPassword
            }
            if (!isSamePassword) {
                Row(
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
                        contentDescription = "Repeat password error icon"
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
                    .align(Alignment.End)
                    .padding(vertical = textPad)
                    .clickable { sendIntent(RegisterIntent.GoToSignIn) },
                fontSize = 14.sp,
                color = salamandraColor,
            )
            Spacer(modifier = Modifier.weight(middlePadWeight))
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(buttonWeight)
                    .border(BorderStroke(2.dp, primaryVariant), RoundedCornerShape(40))
                    .clickable { sendIntent(RegisterIntent.OnRegister) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.register),
                    fontSize = 16.sp,
                    color = primaryVariant,
                )
            }
            Spacer(modifier = Modifier.weight(verticalPadWeight))

        }
        if (state.error != null)
            ErrorDialog(
                error = state.error.asUiText(),
                onDismiss = { sendIntent(RegisterIntent.CloseError) }
            )
    }
}

private fun checkFields(state: RegisterState, isSamePassword: Boolean, isUsernameValid: Boolean) =
    state.isEmailValid && state.passwordFormatError == null && isSamePassword && isUsernameValid && state.email != "" && state.password != "" && state.username != ""



@Preview()
@Composable
private fun ScreenPreview() {
    SalamandraTheme {
        ScreenBody(
            state = RegisterState.initial,
            sendIntent = {},
        )
    }
}