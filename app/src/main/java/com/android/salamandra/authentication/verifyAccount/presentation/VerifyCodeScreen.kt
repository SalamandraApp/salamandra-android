package com.android.salamandra.authentication.verifyAccount.presentation

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.presentation.components.MyImageLogo
import com.android.salamandra.destinations.HomeScreenDestination
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@Composable
fun VerifyCodeScreen(
    navigator: DestinationsNavigator,
    viewModel: VerifyCodeViewModel = hiltViewModel(),
    username: String,
    email: String
) {
    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        when (events) {
            VerifyCodeEvent.NavigateToHome -> navigator.navigate(HomeScreenDestination)
            null -> {}
        }
    }

    viewModel.dispatch(VerifyCodeIntent.SetUsername(username = username))
    viewModel.dispatch(VerifyCodeIntent.SetEmail(email= email))

    ScreenBody(
        state = state,
        sendIntent = viewModel::dispatch
    )
}

@Composable
private fun ScreenBody(
    state: VerifyCodeState,
    sendIntent: (VerifyCodeIntent) -> Unit,
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(tertiary), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val n = 1 // Text fields
            val textFieldWeight = 120f
            val buttonWeight = 100f
            val betweenFieldsWeight = 50f
            val middlePadWeight = 80f

            val verticalPadWeight = (1000f -
                    n * textFieldWeight -
                    2 * middlePadWeight -
                    (n - 1) * betweenFieldsWeight -
                    buttonWeight) / 2

            Spacer(modifier = Modifier.weight(verticalPadWeight))
            MyImageLogo()
            Spacer(modifier = Modifier
                .weight(middlePadWeight)
                .fillMaxWidth())
            OtpTextField(
                modifier = Modifier.weight(textFieldWeight),
                otpText = state.code,
                onOtpTextChange = { value, otpInputFilled ->
                    sendIntent(VerifyCodeIntent.ChangeCode(value))
                }
            )
            val checkMessage =
                        stringResource(id = R.string.check) +
                        " " + state.email +
                        " " + stringResource(id = R.string.for_confirmation_code)
            Text(
                modifier = Modifier.padding(top = 14.dp),
                text = checkMessage,
                color = onTertiary,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(middlePadWeight))

            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(buttonWeight)
                    .border(BorderStroke(2.dp, primaryVariant), RoundedCornerShape(40))
                    .clickable { sendIntent(VerifyCodeIntent.ConfirmCode) },
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
        if (state.error != null)
            ErrorDialog(
                error = state.error.asUiText(),
                onDismiss = { sendIntent(VerifyCodeIntent.CloseError) }
            )
    }
}

@Composable
private fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int = 6,
    onOtpTextChange: (String, Boolean) -> Unit
) {
    Row (modifier = modifier){
        BasicTextField(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
            onValueChange = {
                if (it.text.length <= otpCount) {
                    onOtpTextChange.invoke(it.text, it.text.length == otpCount)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            decorationBox = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(otpCount) { index ->
                        CharView(
                            index = index,
                            text = otpText,
                        )
                        if (index != otpCount - 1)
                            Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        )
    }
}

@Composable
private fun CharView(
    modifier: Modifier = Modifier,
    index: Int,
    text: String
) {
    val isFocused = text.length == index
    val char = when {
        index == text.length -> "-"
        index > text.length -> ""
        else -> text[index].toString()
    }
    val borderPixels = if (isFocused) { 2.dp } else { 1.dp }
    Column (
        modifier = modifier
            .border(
                borderPixels, when {
                    isFocused -> primaryVariant.copy(alpha = 0.7f)
                    else -> onTertiary
                }, RoundedCornerShape(8.dp)
            )
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ){
        Text(
            modifier = Modifier
                .width(40.dp),
            text = char,
            fontSize = 35.sp,
            color = if (isFocused) {
                primaryVariant
            } else {
                title
            },
            textAlign = TextAlign.Center
        )
    }
}


//@Preview(showBackground = true, widthDp = 420, heightDp = 750)
@Preview
@Composable
fun LightPreview() {
    SalamandraTheme {
        ScreenBody(
            state = VerifyCodeState.initial,
            sendIntent = {},
        )
    }
}
