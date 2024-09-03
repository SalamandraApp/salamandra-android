package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.title

@Composable
fun EditWeight(
    weight: Double,
    onEditWeight: (Double) -> Unit
) {
    val parts = weight.toString().split(".")
    val beforeDot = remember { mutableStateOf(weight % 1.0 == 0.0) }
    val int = remember { mutableStateOf(parts[0]) }
    val decimal = remember { mutableStateOf(parts[1]) }
    val weightString = "${int.value}.${decimal.value}"
    val styledWeight = buildAnnotatedString {
        append(int.value)
        withStyle(style = SpanStyle(color = if (beforeDot.value) onTertiary else title)) {
            append(".")
        }
        withStyle(style = SpanStyle(color = if (!beforeDot.value && decimal.value != "0") title else onTertiary)) {
            append(decimal.value)
        }
    }
    NumberField(
        modifier = Modifier.clip(RoundedCornerShape(10.dp)),
        value = styledWeight,
        onValueChange = {
            val lastChar = it.last()
            // Removed
            if (it.length < weightString.length) {
                if (beforeDot.value ) {
                    int.value = if (int.value.length > 1) int.value.dropLast(1) else "0"
                } else {
                    if (decimal.value == "0") beforeDot.value = true
                    decimal.value = "0"
                }
            }
            // Added
            else {
                // Dot
                if (lastChar.toString() == ".") {
                    beforeDot.value = false
                }
                // Other
                else if (lastChar.digitToIntOrNull() != null) {
                    if (beforeDot.value) {
                        if (int.value == "0") int.value = ""
                        int.value += lastChar
                    } else {
                        decimal.value = lastChar.toString()
                    }
                }
            }
            onEditWeight("${int.value}.${decimal.value}".toDouble())
        }
    )
}

