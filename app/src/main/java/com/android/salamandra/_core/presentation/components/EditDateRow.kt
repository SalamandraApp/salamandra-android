package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.android.salamandra.R
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.subtitle
import com.android.salamandra.ui.theme.textFieldColors
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDateRow(
    modifier: Modifier = Modifier,
    date: LocalDate,
    onSave: (LocalDate?) -> Unit,
) {
    val currentYear = LocalDate.now().year
    val yearRange = IntRange(currentYear - 100, currentYear)
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input,
        initialSelectedDateMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        yearRange = yearRange
    )
    val selectedLocalDate: LocalDate? = datePickerState.selectedDateMillis?.let { millis ->
        Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    val dateText = buildAnnotatedString {
        append(stringResource(R.string.birthday) + ": ")
        withStyle(style = SpanStyle(color = onSecondary)) {
            append(selectedLocalDate?.toString() ?: stringResource(R.string.not_specified))
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
                        TextButton (onClick = { showDialog.value = false }) {
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
                        navigationContentColor = subtitle,
                        yearContentColor = subtitle,
                        disabledYearContentColor = subtitle,
                        currentYearContentColor = subtitle,
                        selectedYearContentColor = subtitle,
                        disabledSelectedYearContentColor = subtitle,
                        selectedYearContainerColor = subtitle,
                        disabledSelectedYearContainerColor = subtitle,
                        dayContentColor = subtitle,
                        disabledDayContentColor = subtitle,
                        selectedDayContentColor = subtitle,
                        disabledSelectedDayContentColor = subtitle,
                        selectedDayContainerColor = subtitle,
                        disabledSelectedDayContainerColor = subtitle,
                        todayContentColor = subtitle,
                        todayDateBorderColor = subtitle,
                        dayInSelectionRangeContentColor = subtitle,
                        dayInSelectionRangeContainerColor = subtitle,
                        dividerColor = subtitle,
                        dateTextFieldColors = textFieldColors()
                    )

                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false,
                    )
                }
            }
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