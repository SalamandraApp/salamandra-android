package com.android.salamandra.authentication.commons.presentation.textfields

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.android.salamandra.R
import com.android.salamandra.ui.theme.NormalTypo


@Composable
fun MyAuthTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    textResource: Int,

    ) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onValueChange(it) },
        label = {
            Text(
                text = stringResource(textResource),
                fontSize = 16.sp,
                style = NormalTypo
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email
        ),
        singleLine = true,
        shape = RoundedCornerShape(40),
    )
}
