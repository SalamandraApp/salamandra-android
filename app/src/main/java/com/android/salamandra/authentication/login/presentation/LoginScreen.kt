package com.android.salamandra.authentication.login.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import com.android.salamandra._core.presentation.components.MyColumn
import com.android.salamandra._core.presentation.components.MyImageLogo
import com.android.salamandra.authentication.commons.presentation.textfields.MyEmailTextField
import com.android.salamandra.authentication.commons.presentation.textfields.MyPasswordTextField
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.salamandraColor
import com.android.salamandra.workouts.editWk.presentation.EditWkIntent
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        MyColumn(modifier = Modifier.offset(y = (-30).dp)) {
            MyImageLogo()
            MyEmailTextField(
                modifier = Modifier,
                value = state.email,
                onValueChange = {
                    sendIntent(LoginIntent.ChangeEmail(it))
                }
            )
            MyPasswordTextField(
                modifier = Modifier,
                value = state.password,
                hint = stringResource(R.string.password),
                onValueChange = { sendIntent(LoginIntent.ChangePassword(it)) }
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(R.string.don_t_have_an_account_register),
                color = salamandraColor,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { sendIntent(LoginIntent.GoToSignup) }
                    .padding(4.dp)
            )
        }
        OutlinedButton(
            onClick = {sendIntent(LoginIntent.Login)},
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
                .padding(bottom = 12.dp)
                .fillMaxWidth(),
            border = BorderStroke(1.dp, salamandraColor),
            shape = RoundedCornerShape(42)
        ) {
            Text(
                text = stringResource(R.string.login),
                fontSize = 16.sp,
                color = salamandraColor,
                modifier = Modifier.padding(4.dp)
            )
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