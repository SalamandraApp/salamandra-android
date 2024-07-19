package com.android.salamandra.workouts.editWk.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.android.salamandra.R
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.presentation.components.NumberField
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.textFieldColors
import com.android.salamandra.ui.theme.title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWkTemplateElement(
    element: WkTemplateElement,
    index: Int,
    onEditSets: (Int, Int) -> Unit,
    onEditReps: (Int, Int) -> Unit,
    onEditWeight: (Double, Int) -> Unit,
    onEditRest: (Int, Int) -> Unit,
    onDeleteElement: (Int) -> Unit,
) {
    Column (
        modifier = Modifier.imePadding()
    ){
        val keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
        Row {
            Text(
                text = element.exercise.name,
                fontSize = 22.sp,
                style = TitleTypo,
                color = title
            )
        }
        val wSpacer = 0.3f
        val wField = 1f
        val labelColor = onTertiary.copy(0.6f)
        Row(
            modifier = Modifier.padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(wField)) {
                Text(text = stringResource(R.string.sets), color = labelColor)
            }
            Spacer(modifier = Modifier.weight(wSpacer))
            Box(modifier = Modifier.weight(wField)) {
                Text(text = stringResource(R.string.reps), color = labelColor)
            }
            Spacer(modifier = Modifier.weight(wSpacer))
            Box(modifier = Modifier.weight(wField + wSpacer / 2)) {
                if (element.weight != null) Text(
                    text = stringResource(R.string.weight_kg),
                    color = labelColor
                )
            }

        }
        Row(
            modifier = Modifier.padding(top = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(wField)
            ) {
                NumberField(
                    modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                    value = element.sets.toString(),
                    colors = if (element.sets != 0) {
                        textFieldColors(false)
                    } else {
                        textFieldColors(false).copy(
                            unfocusedTextColor = onTertiary,
                            focusedTextColor = onTertiary
                        )
                    },
                    onValueChange = {
                        val newInt = it.toIntOrNull() ?: 0
                        if (element.sets == 0 && newInt >= 10) {
                            onEditSets(newInt / 10, index)
                        } else {
                            onEditSets(newInt, index)
                        }
                    }
                )
            }
            Icon(
                modifier = Modifier.weight(wSpacer),
                imageVector = Icons.Outlined.Close,
                tint = onTertiary,
                contentDescription = "Search workout"
            )
            Box(modifier = Modifier.weight(wField)) {
                NumberField(
                    modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                    value = element.reps.toString(),
                    colors = if (element.reps != 0) {
                        textFieldColors(false)
                    } else {
                        textFieldColors(false).copy(
                            unfocusedTextColor = onTertiary,
                            focusedTextColor = onTertiary
                        )
                    },
                    onValueChange = {
                        val newInt = it.toIntOrNull() ?: 0
                        if (element.reps == 0 && newInt >= 10) {
                            onEditReps(newInt / 10, index)
                        } else {
                            onEditReps(newInt, index)
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.weight(wSpacer / 2))
            Box(
                modifier = Modifier.weight(wSpacer / 2),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "(",
                    fontSize = 26.sp,
                    color = onTertiary
                )
            }
            Row(
                modifier = Modifier.weight(wField),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (element.weight != null) {
                    val parts = element.weight.toString().split(".")
                    val beforeDot = remember { mutableStateOf(element.weight % 1.0 == 0.0) }
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
                                if (beforeDot.value) {
                                    int.value =
                                        if (int.value.length > 1) int.value.dropLast(1) else "0"
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

                            onEditWeight("${int.value}.${decimal.value}".toDouble(), index)
                        }
                    )
                }
            }
            Box(
                modifier = Modifier.weight(wSpacer / 2),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ")",
                    fontSize = 26.sp,
                    color = onTertiary
                )
            }

        }

        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val checkColor = onTertiary.copy(0.7f)
            val checkedReps = remember { mutableStateOf(false) }
            val checkedWeight = remember { mutableStateOf(false) }
            val checkBoxColors = CheckboxDefaults.colors(
                checkedColor = checkColor,
                uncheckedColor = checkColor,
                checkmarkColor = tertiary
            )
            Text(
                modifier = Modifier.weight(wField + wSpacer),
                text = stringResource(R.string.change_through_set),
                style = NormalTypo,
                color = checkColor,
                fontSize = 15.sp
            )
            Checkbox(
                modifier = Modifier.weight(wField),
                checked = checkedReps.value,
                onCheckedChange = { checkedReps.value = it },
                colors = checkBoxColors
            )
            Spacer(modifier = Modifier.weight(wSpacer))
            if (element.weight != null)
                Checkbox(
                    modifier = Modifier.weight(wField),
                    checked = checkedWeight.value,
                    onCheckedChange = { checkedWeight.value = it },
                    colors = checkBoxColors
                )
            val fillCheckbox = if (element.weight != null) 0f else wField
            Spacer(modifier = Modifier.weight(wSpacer / 2 + fillCheckbox))

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                modifier = Modifier.weight(wField).padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.rest), color = labelColor)
                val minutes = (element.rest / 60).toInt()
                val seconds = (element.rest % 60).toInt()
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = String.format("%d:%02d", minutes, seconds),
                    fontSize = 18.sp,
                    style = TitleTypo,
                )
            }
            Spacer(modifier = Modifier.weight(wSpacer))
            Box (modifier = Modifier.weight(2 * wField + wSpacer)) {
                val sliderPosition = remember { mutableFloatStateOf(0f) }
                Slider(
                    value = sliderPosition.value,
                    onValueChange = {
                        sliderPosition.value = it
                        onEditRest(it.toInt(), index)
                    },
                    steps = 19,
                    valueRange = 0f..300f,
                    colors = SliderDefaults.colors(
                        thumbColor = primaryVariant,
                        activeTrackColor = primaryVariant,
                        activeTickColor = primary,
                    )
                )
            }
        }
        Row {
            Box(modifier = Modifier
                .padding(top = 8.dp)
                .weight(2 * wField + wSpacer)) {
                ExtendedFloatingActionButton(
                    containerColor = secondary,
                    contentColor = colorError,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    onClick = { onDeleteElement(index) }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete exercise",
                    )
                    Text(
                        text = stringResource(R.string.delete_exercise),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}