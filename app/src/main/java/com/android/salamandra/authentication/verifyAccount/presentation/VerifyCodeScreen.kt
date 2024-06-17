package com.android.salamandra.authentication.verifyAccount.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.presentation.components.MyColumn
import com.android.salamandra._core.presentation.components.MyImageLogo
import com.android.salamandra._core.presentation.components.MySpacer
import com.android.salamandra._core.presentation.components.textFields.MyOutlinedTextField
import com.android.salamandra.ui.theme.salamandraColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@Composable
fun VerifyCodeScreen(
    navigator: DestinationsNavigator,
    viewModel: VerifyCodeViewModel = hiltViewModel(),
    username: String
) {
    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        when (events) {
            else -> {}
        }
    }

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
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        MyColumn(
            modifier = Modifier
                .offset(y = (-32).dp)
                .padding(12.dp)
        ) {
            MyImageLogo()
            MyOutlinedTextField(
                modifier = Modifier,
                value = state.code,
                hint = stringResource(R.string.confirmation_code),
                onValueChange = {
                    sendIntent(VerifyCodeIntent.ChangeCode(it))
                }
            )
            MySpacer(size = 8)
            Text(
                text = stringResource(R.string.check_your_email_for_confirmation_code),
                color = MaterialTheme.colorScheme.onBackground
            )

        }

        OutlinedButton(
            onClick = {
                sendIntent(VerifyCodeIntent.ConfirmCode)
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
                text = "Confirm",
                fontSize = 16.sp,
                color = salamandraColor,
                modifier = Modifier.padding(4.dp)
            )
        }
        if (state.error != null) ErrorDialog(error = state.error.asUiText())
    }
}