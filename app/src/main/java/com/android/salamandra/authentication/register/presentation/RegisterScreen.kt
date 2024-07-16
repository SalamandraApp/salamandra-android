package com.android.salamandra.authentication.register.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.presentation.components.MyCircularProgressbar
import com.android.salamandra._core.presentation.components.SlmLogo
import com.android.salamandra.authentication.verifyAccount.presentation.VerifyCodeNavArgs
import com.android.salamandra.destinations.LoginScreenDestination
import com.android.salamandra.destinations.ProfileScreenDestination
import com.android.salamandra.destinations.VerifyCodeScreenDestination
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.salamandraColor
import com.android.salamandra.ui.theme.subtitle
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title
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
                    VerifyCodeNavArgs(username = state.username, email = state.email, password = state.password)
                )
            )
            RegisterEvent.NavigateToProfile -> navigator.navigate(ProfileScreenDestination)
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
                .fillMaxHeight()
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val underLogoSpacer = 30.dp
            val betweenFieldSpacer = 20.dp
            val overButtonSpacer = 30.dp
            val buttonHeight = 50.dp
            val textPad = 10.dp
            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = title,
                focusedBorderColor = primaryVariant,
                focusedLabelColor = primaryVariant,

                unfocusedTextColor = subtitle,
                unfocusedBorderColor = onTertiary,
                unfocusedLabelColor = subtitle,
            )

            SlmLogo()
            Spacer(modifier = Modifier.height(underLogoSpacer))

            // -------------------------------- USERNAME
            val allowedChars = "^[a-zA-Z0-9_.]*$"
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.username,
                onValueChange = {
                    isUsernameValid = it.matches(allowedChars.toRegex())
                    sendIntent(RegisterIntent.ChangeUsername(it))
                },
                label = {
                    Text(
                        text = stringResource(R.string.username),
                        fontSize = 16.sp,
                        style = NormalTypo
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(40),
                colors = textFieldColors
            )
            if (!isUsernameValid) {
                Row(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .height(betweenFieldSpacer)
                        .padding(top = 4.dp),
                ) {
                    Icon(
                        modifier = Modifier
                            .size(17.dp)
                            .align(Alignment.CenterVertically),
                        imageVector = Icons.Outlined.ErrorOutline,
                        tint = colorError,
                        contentDescription = "username error icon"
                    )
                    Text(
                        text = stringResource(R.string.invalid_username),
                        color = colorError,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp),
                        style = SemiTypo,
                        fontSize = 12.sp
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(betweenFieldSpacer))
            }

            // -------------------------------- EMAIL
            OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
            value = state.email,
            onValueChange = {
                sendIntent(RegisterIntent.ChangeEmail(it))
            },
            label = {
                Text(
                    text = stringResource(R.string.email),
                    fontSize = 16.sp,
                    style = NormalTypo
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            singleLine = true,
            shape = RoundedCornerShape(40),
            colors = textFieldColors
            )
            if (!state.isEmailValid) {
                Row(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .height(betweenFieldSpacer)
                        .padding(top = 4.dp),
                ) {
                    Icon(
                        modifier = Modifier
                            .size(17.dp)
                            .align(Alignment.CenterVertically),
                        imageVector = Icons.Outlined.ErrorOutline,
                        tint = colorError,
                        contentDescription = "email error icon"
                    )
                    Text(
                        text = stringResource(R.string.email_must_have_a_valid_format),
                        color = colorError,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp),
                        style = SemiTypo,
                        fontSize = 12.sp
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(betweenFieldSpacer))
            }
            var passwordVisibility by remember { mutableStateOf(false) }
            val img = if (passwordVisibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.password,
                onValueChange = { sendIntent(RegisterIntent.ChangePassword(it)) },
                label = {
                    Text(
                        text = stringResource(R.string.password),
                        fontSize = 16.sp,
                        style = NormalTypo
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                shape = RoundedCornerShape(40),
                singleLine = true,
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(imageVector = img, contentDescription = null)
                    }
                },
                colors = textFieldColors
            )

            if (state.passwordFormatError != null) {
                Row(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .height(betweenFieldSpacer)
                        .padding(top = 4.dp),
                ) {
                    Icon(
                        modifier = Modifier
                            .size(17.dp)
                            .align(Alignment.CenterVertically),
                        imageVector = Icons.Outlined.ErrorOutline,
                        tint = colorError,
                        contentDescription = "password error icon"
                    )
                    Text(
                        text = state.passwordFormatError.asUiText().asString(),
                        color = colorError,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp),
                        style = SemiTypo,
                        fontSize = 12.sp
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(betweenFieldSpacer))
            }


            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = repeatPassword,
                onValueChange = {
                    repeatPassword = it
                },
                label = {
                    Text(
                        text = stringResource(R.string.repeat_password),
                        fontSize = 16.sp,
                        style = NormalTypo
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                shape = RoundedCornerShape(40),
                singleLine = true,
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                colors = textFieldColors
            )

            LaunchedEffect(key1 = state.password) {
                isSamePassword = state.password == repeatPassword
            }

            if (!isSamePassword) {
                Row(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .height(betweenFieldSpacer)
                        .padding(top = 4.dp),
                ) {
                    Icon(
                        modifier = Modifier
                            .size(17.dp)
                            .align(Alignment.CenterVertically),
                        imageVector = Icons.Outlined.ErrorOutline,
                        tint = colorError,
                        contentDescription = "repeat password error icon"
                    )
                    Text(
                        text = stringResource(R.string.passwords_must_coincide),
                        color = colorError,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp),
                        style = SemiTypo,
                        fontSize = 12.sp
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
            if (isSamePassword) Spacer(modifier = Modifier.height(overButtonSpacer))

            val canRegister = isSamePassword &&
                    isUsernameValid &&
                    state.isEmailValid &&
                    (state.passwordFormatError == null)

            val registerButton = if (canRegister) primaryVariant else onTertiary
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight)
                    .border(BorderStroke(2.dp, registerButton), RoundedCornerShape(40))
                    .clickable { if (canRegister) sendIntent(RegisterIntent.OnRegister) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.register),
                    fontSize = 16.sp,
                    color = registerButton,
                )
            }

        }
        IconButton(modifier = Modifier
            .align(Alignment.TopStart)
            .padding(start = 12.dp, top = 12.dp), onClick = { sendIntent(RegisterIntent.GoToHomeNoRegister) }) {
            Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close login", tint = onSecondary)
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
private fun RegisterScreenPreview() {
    SalamandraTheme {
        ScreenBody(
            state = RegisterState.initial,
            sendIntent = {},
        )
    }
}
