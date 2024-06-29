package com.android.salamandra.authentication.commons.presentation.textfields

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.android.salamandra.R
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.subtitle
import com.android.salamandra.ui.theme.title


@Composable
fun MyAuthTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    textResource: Int,
    roundCorner: Int = 40

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
        shape = RoundedCornerShape(roundCorner),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = title,
            focusedBorderColor = primaryVariant,
            focusedLabelColor = primaryVariant,

            unfocusedTextColor = subtitle,
            unfocusedBorderColor = onTertiary,
            unfocusedLabelColor = subtitle,

        )
    )
}
