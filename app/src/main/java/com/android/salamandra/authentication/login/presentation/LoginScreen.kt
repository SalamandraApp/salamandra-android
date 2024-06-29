package com.android.salamandra.authentication.login.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.android.salamandra._core.presentation.components.MyCircularProgressbar
import com.android.salamandra._core.presentation.components.MyImageLogo
import com.android.salamandra.authentication.commons.presentation.textfields.MyAuthTextField
import com.android.salamandra.authentication.commons.presentation.textfields.MyPasswordTextField
import com.android.salamandra.destinations.HomeScreenDestination
import com.android.salamandra.destinations.ProfileScreenDestination
import com.android.salamandra.destinations.RegisterScreenDestination
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.tertiary
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
            LoginEvent.NavigateUp -> navigator.navigate(ProfileScreenDestination)
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
    val textPad = 12.dp;
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(tertiary),
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val n = 2 // Text fields
            val textFieldWeight = 100f
            val buttonWeight = 100f
            val betweenFieldsWeight = 50f
            val middlePadWeight = 50f

            val verticalPadWeight = (1000f -
                    n * textFieldWeight -
                    2 * middlePadWeight -
                    (n - 1) * betweenFieldsWeight -
                    buttonWeight) / 2

            Spacer(modifier = Modifier.weight(verticalPadWeight))
            MyImageLogo()
            Spacer(modifier = Modifier.weight(middlePadWeight))
            MyAuthTextField(
                modifier = Modifier
                    .weight(textFieldWeight)
                    .fillMaxWidth(),
                value = state.email,
                onValueChange = {
                    sendIntent(LoginIntent.ChangeEmail(it))
                },
                textResource = R.string.username_or_email,
                roundCorner = 40
            )
            Spacer(modifier = Modifier.weight(betweenFieldsWeight))
            MyPasswordTextField(
                modifier = Modifier
                    .weight(textFieldWeight)
                    .fillMaxWidth(),
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
                    .padding(vertical = textPad)
            )

            Spacer(modifier = Modifier.weight(middlePadWeight))
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(buttonWeight)
                    .border(BorderStroke(2.dp, primaryVariant), RoundedCornerShape(40))
                    .clickable { sendIntent(LoginIntent.Login) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.login),
                    fontSize = 16.sp,
                    color = primaryVariant,
                )
            }
            Spacer(modifier = Modifier.weight(verticalPadWeight))
        }
        IconButton(modifier = Modifier.align(Alignment.TopStart).padding(start = 12.dp, top = 12.dp), onClick = { sendIntent(LoginIntent.GoToHomeNoSignIn) }) {
            Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close login", tint = onSecondary)
        }
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
