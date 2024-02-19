package com.android.salamandra.ui.login

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
import androidx.compose.foundation.selection.selectable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra.ui.components.MyColumn
import com.android.salamandra.ui.components.MyEmailTextField
import com.android.salamandra.ui.components.MyImageLogo
import com.android.salamandra.ui.components.MyPasswordTextField
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.salamandraColor
import com.destinations.HomeScreenDestination
import com.destinations.RegisterScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    ScreenBody(
        onLogin = {email, password ->
            loginViewModel.onLogin(username = email, password = password)
            navigator.navigate(HomeScreenDestination)
        },
        onRegister = { navigator.navigate(RegisterScreenDestination) }
    )
}

@Composable
private fun ScreenBody(
    onLogin: (String, String) -> Unit,
    onRegister: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        var email by remember { mutableStateOf("jaimevzkz1@gmail.com") }
        var password by remember { mutableStateOf("1234Qwerty") }


        MyColumn(modifier = Modifier.offset(y = (-30).dp)) {
            MyImageLogo()
            MyEmailTextField(modifier = Modifier, text = email, onTextChanged = { email = it })
            MyPasswordTextField(
                modifier = Modifier,
                text = password,
                onTextChanged = { password = it }
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(R.string.don_t_have_an_account_register),
                color = salamandraColor,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onRegister() }
                    .padding(4.dp)
            )
        }
        OutlinedButton(
            onClick = { onLogin(email, password) },
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
    }
}

@Preview
@Composable
fun LightPreview() {
    SalamandraTheme {
        ScreenBody(
            onLogin = {_,_ ->},
            onRegister = {}
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