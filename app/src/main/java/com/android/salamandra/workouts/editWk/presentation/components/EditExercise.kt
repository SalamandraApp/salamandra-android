package com.android.salamandra.workouts.editWk.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra.R
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.util.WORKOUT_TEMPLATE_ELEMENT
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.textFieldColors
import com.android.salamandra.ui.theme.title
import com.android.salamandra.workouts.editWk.presentation.EditWkIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExercise(
    // TODO, delete placeholder
    templateElement: WkTemplateElement = WORKOUT_TEMPLATE_ELEMENT,
    index: Int,
    sendIntent: (EditWkIntent) -> Unit
) {

    val keyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Number
    )
    Row {
        Text(
            text = templateElement.exercise.name,
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
            Text(text = stringResource(R.string.weight_kg), color = labelColor)
        }

    }
    Row(
        modifier = Modifier.padding(top = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(wField)
        ) {
            TextField(
                modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                singleLine = true,
                enabled = true,
                value = templateElement.sets.toString(),
                textStyle = TitleTypo.copy(fontSize = 20.sp),
                colors = textFieldColors(),
                keyboardOptions = keyboardOptions,
                onValueChange = {
                    val newSets = it.toIntOrNull()
                    if (newSets != null) {
                        // sendIntent(EditWkIntent.ChangeWkElementSets(newSets, index))
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
            TextField(
                modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                singleLine = true,
                enabled = true,
                value = templateElement.reps.toString(),
                textStyle = TitleTypo.copy(fontSize = 20.sp),
                colors = textFieldColors(),
                keyboardOptions = keyboardOptions,
                onValueChange = {
                    val newReps = it.toIntOrNull()
                    if (newReps != null) {
                        // sendIntent(EditWkIntent.ChangeWkElementReps(newReps, index))
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
            TextField(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp)),
                singleLine = true,
                enabled = true,
                value = templateElement.weight.toString(),
                textStyle = TitleTypo.copy(fontSize = 20.sp),
                colors = textFieldColors(),
                keyboardOptions = keyboardOptions,
                onValueChange = {
                    val newWeight = it.toDoubleOrNull()
                    if (newWeight != null) {
                        // sendIntent(EditWkIntent.ChangeWkElementWeight(newWeight, index))
                    }
                }
            )

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
        Checkbox(
            modifier = Modifier.weight(wField),
            checked = checkedWeight.value,
            onCheckedChange = { checkedWeight.value = it },
            colors = checkBoxColors
        )
        Spacer(modifier = Modifier.weight(wSpacer / 2))

    }

    Row(
        modifier = Modifier.padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(wField)) {
            Text(text = stringResource(R.string.rest), color = labelColor)
        }
    }
    Row(
        modifier = Modifier.padding(top = 5.dp)
    ) {
        // TODO, placeholder
        val time = remember { mutableStateOf("0") }
        TextField(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp)),
            singleLine = true,
            enabled = true,
            value = time.value,
            // value = templateElement.rest.toString(),
            textStyle = TitleTypo.copy(fontSize = 20.sp),
            colors = textFieldColors(),
            keyboardOptions = keyboardOptions,
            onValueChange = {
            },
            placeholder = { Text("MM:SS") }
        )
        Spacer(modifier = Modifier.weight(wSpacer))
        Box(modifier = Modifier.weight(2 * wField + wSpacer)) {
            ExtendedFloatingActionButton(
                containerColor = secondary,
                contentColor = colorError,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                onClick = { }) {
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