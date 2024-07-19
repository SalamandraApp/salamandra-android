package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.textFieldColors


@Composable
fun NumberField(
    modifier: Modifier = Modifier,
    value: String,
    colors: TextFieldColors = textFieldColors(false),
    fontSize: TextUnit = 20.sp,
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit)? = null
) {
    TextField(
        modifier = modifier,
        singleLine = true,
        enabled = true,
        value = TextFieldValue(text = value, selection = TextRange(value.length)),
        textStyle = TitleTypo.copy(fontSize = fontSize),
        colors = colors,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        onValueChange = {
            onValueChange(it.text)
        },
        placeholder = placeholder
    )
}
@Composable
fun NumberField(
    modifier: Modifier = Modifier,
    value: AnnotatedString,
    colors: TextFieldColors = textFieldColors(false).copy(
        focusedTextColor = Color.Transparent,
        unfocusedTextColor = Color.Transparent
    ),
    fontSize: TextUnit = 20.sp,
    onValueChange: (String) -> Unit,
) {
    Box (
        contentAlignment = Alignment.CenterStart
    )
    {
        TextField(
            modifier = modifier,
            singleLine = true,
            enabled = true,
            value = TextFieldValue(text = value.text, selection = TextRange(value.text.length)),
            textStyle = TitleTypo.copy(fontSize = fontSize),
            colors = colors,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = { newValue: TextFieldValue ->
                onValueChange(newValue.text)
            },
        )
        Text(
            modifier = Modifier.zIndex(1f).padding(start = 14.dp),
            text = value,
            fontSize = fontSize,
            style = TitleTypo,
            maxLines = 1
        )
    }
}
