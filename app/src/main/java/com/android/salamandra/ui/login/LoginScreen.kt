package com.android.salamandra.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra.ui.components.MyColumn
import com.android.salamandra.ui.components.MyEmailTextField
import com.android.salamandra.ui.components.MyImageLogo
import com.android.salamandra.ui.components.MyPasswordTextField
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.salamandraColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun LoginScreen(navigator: DestinationsNavigator) {
    ScreenBody()
}

@Composable
private fun ScreenBody() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
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
                text = "Don't have an account? Register",
                modifier = Modifier.align(Alignment.End),
                color = salamandraColor,
                fontSize = 14.sp
            )
        }
        OutlinedButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
                .padding(bottom = 12.dp)
                .fillMaxWidth(),
            border = BorderStroke(1.dp, salamandraColor),
            shape = RoundedCornerShape(42)
        ) {
            Text(text = "Login", fontSize = 16.sp, color = salamandraColor, modifier = Modifier.padding(4.dp))
        }
    }
}

@Preview
@Composable
fun LightPreview() {
    SalamandraTheme {
        ScreenBody()
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//    SalamandraTheme {
//        ScreenBody()
//    }
//}