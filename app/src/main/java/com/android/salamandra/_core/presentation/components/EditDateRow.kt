package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EditOff
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.subtitle
import com.android.salamandra.workouts.editWk.presentation.components.MAX_NAME_LENGTH
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDateRow(
    modifier: Modifier = Modifier,
    date: LocalDate,
    onSave: (LocalDate) -> Unit,
) {
    val currentYear = LocalDate.now().year
    val yearRange = IntRange(currentYear - 100, currentYear)
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        yearRange = yearRange
    )
    val selectedLocalDate: LocalDate? = datePickerState.selectedDateMillis?.let { millis ->
        Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    val dateText = buildAnnotatedString {
        append("DOB: ")
        withStyle(style = SpanStyle(color = onSecondary)) {
            append(selectedLocalDate.toString())
        }
    }

    val editState = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(true) }
    Row (
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        if (!editState.value) {
            Text(
                text = dateText,
                color = subtitle,
                style = TitleTypo,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.weight(1f))
            Icon(
                modifier = Modifier.clickable { editState.value = true },
                tint = onSecondary,
                imageVector = Icons.Outlined.Edit,
                contentDescription = null
            )
        } else {
            if (showDialog.value) {
                DatePickerDialog(
                    onDismissRequest = { showDialog.value = false },
                    confirmButton = {
                        TextButton (
                            onClick = {
                                // Handle confirm click
                                showDialog.value = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDialog.value = false }
                        ) {
                            Text("Cancel")
                        }
                    },
                    modifier = Modifier.padding(32.dp),
                    properties = DialogProperties(usePlatformDefaultWidth = false),
                    colors = DatePickerDefaults.colors(
                        containerColor = secondary,
                        titleContentColor = subtitle,
                        headlineContentColor = subtitle,
                        weekdayContentColor = subtitle,
                        subheadContentColor = subtitle,
                )
                ) {
                    DatePicker(
                        state = datePickerState,
                    )
                }
            }
            if (selectedLocalDate != null){
                Row (Modifier.weight(0.8f), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = dateText,
                        color = subtitle,
                        style = TitleTypo,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(10.dp))
                    IconButton({ showDialog.value = true }) {
                        Icon(
                            tint = onSecondary,
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null
                        )
                    }
                }
                Spacer(Modifier.width(10.dp))
                AnimatedFloatingButton(
                    modifier = Modifier.weight(0.2f).padding(start = 10.dp),
                    initialIcon = Icons.Filled.CheckCircle,
                    pressedIcon = Icons.Filled.CheckCircle,
                    delayTime = 400,
                    initialContainerColor = onSecondary.copy(0.2f),
                    pressedContainerColor = primaryVariant.copy(0.3f),
                    initialIconColor = onSecondary,
                    pressedIconColor = primaryVariant,
                    onPress = {
                        showDialog.value = false
                        editState.value = false
                        onSave(selectedLocalDate)
                    },
                    instant = false
                )
            }
        }
    }
}