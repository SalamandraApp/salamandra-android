package com.android.salamandra.ui.register

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.android.salamandra.ui.components.MyColumn
import com.android.salamandra.ui.components.MyEmailTextField
import com.android.salamandra.ui.components.MyGenericTextField
import com.android.salamandra.ui.components.MyImageLogo
import com.android.salamandra.ui.components.MyPasswordTextField
import com.android.salamandra.ui.components.MySpacer
import com.android.salamandra.ui.components.validateEmail
import com.android.salamandra.ui.components.validatePassword
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.salamandraColor
import com.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun RegisterScreen(
    navigator: DestinationsNavigator,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    ScreenBody(
        onRegister = { registerViewModel.onRegister(username = "user", password = "1234") },
        onSignIn = { navigator.navigate(LoginScreenDestination) }
    )
}

@Composable
private fun ScreenBody(
    onSignIn: () -> Unit,
    onRegister: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var repeatPassword by remember { mutableStateOf("") }
        var nickname by remember { mutableStateOf("") }
        //Validation
        var isEmailValid by remember { mutableStateOf(true) }
        var isPasswordValid by remember { mutableStateOf(true) }
        var isSamePassword by remember { mutableStateOf(true) }
        var isNicknameValid by remember { mutableStateOf(true) }

        MyColumn {
            MyImageLogo()
            MyEmailTextField(modifier = Modifier, text = email, onTextChanged = {
                isEmailValid = validateEmail(it)
                email = it
            })
            if (!isEmailValid) {
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
                text = password,
                onTextChanged = {
                    isPasswordValid = validatePassword(it)
                    password = it
                },
                modifier = Modifier
            )
            if (!isPasswordValid) {
                Text(
                    text = stringResource(R.string.invalid_password_capital_required),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(2.dp)
                )
            } else MySpacer(size = 8)
            MyPasswordTextField(
                modifier = Modifier,
                text = repeatPassword,
                hint = stringResource(R.string.repeat_password),
                onTextChanged = {
                    repeatPassword = it
                    isSamePassword = repeatPassword == password
                })
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
            MyGenericTextField(
                modifier = Modifier,
                text = nickname,
                hint = stringResource(R.string.user_name),
                onTextChanged = {
                    nickname = it
                    isNicknameValid = (nickname != "")
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
                    .clickable { onSignIn() }
                    .align(Alignment.End)
                    .padding(4.dp),
                fontSize = 14.sp,
                color = salamandraColor,
            )
        }
        OutlinedButton(
            onClick = {

//                if (isEmailValid && isPasswordValid && isSamePassword && isNicknameValid) {
//                    //TODO
//                }
                onRegister()
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
    }
}

@Preview
@Composable
fun LightPreview() {
    SalamandraTheme {
        ScreenBody(
            onRegister = {},
            onSignIn = {}
        )
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//    SalamandraTheme {
//        ScreenBody()
//    }
//}