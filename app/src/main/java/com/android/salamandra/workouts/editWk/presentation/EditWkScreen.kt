package com.android.salamandra.workouts.editWk.presentation

import android.content.res.Configuration
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.presentation.components.MyColumn
import com.android.salamandra._core.presentation.components.MyRow
import com.android.salamandra._core.presentation.components.MySpacer
import com.android.salamandra._core.presentation.components.WkPlaceholder
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.primary
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(navArgsDelegate = EditWkNavArgs::class)
@Composable
fun EditWkScreen(navigator: DestinationsNavigator, viewModel: EditWkViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        when (events) {
            else -> {}
        }
    }

    ScreenBody(
        state = state,
        sendIntent = viewModel::dispatch
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenBody(
    state: EditWkState,
    sendIntent: (EditWkIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(tertiary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            MyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .weight(0.8f), verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    WkPlaceholder(size = 110, shape = RoundedCornerShape(0))
                    MySpacer(size = 12)
                    Column(modifier = Modifier) {
                        WkNameTextField(state.wkTemplate.name, sendIntent)
                        MySpacer(size = 4)
                        WkDescriptionTextField(
                            modifier = Modifier.weight(1f),
                            state.wkTemplate.description,
                            sendIntent
                        )
                    }
                }

            }
            MyColumn(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(0.5f)
                    .fillMaxWidth()
            ) {
                MyRow {
                    MyRow(modifier = Modifier.clickable { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete Wk",
                            tint = colorError
                        )
                        MySpacer(size = 2)
                        Text(text = "Remove", color = colorError)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20))
                            .background(secondary),
                        onClick = { /*TODO*/ }) {
                        MyRow {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = "Add Exercise",
                                tint = onSecondary
                            )
                        }
                        Text(text = "ADD EXERCISE", color = onSecondary)
                    }
                    MySpacer(size = 8)
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = "Finish",
                            tint = primary
                        )
                    }
                }
                MySpacer(size = 8)
                Row(Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(0.1f))
                    Text(text = "Exercise", color = onSecondary)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "Reps", color = onSecondary)
                    Spacer(modifier = Modifier.weight(0.1f))
                    Text(text = "Sets", color = onSecondary)
                    Spacer(modifier = Modifier.weight(0.1f))
                    Text(text = "Weight", color = onSecondary)
                    MySpacer(size = 2)
                    Text(
                        modifier = Modifier.align(Alignment.Bottom),
                        text = "(Kg)",
                        color = onSecondary,
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.weight(0.1f))

                }

            }

            Box(
                modifier = Modifier
                    .background(secondary)
                    .weight(2.8f)
                    .fillMaxWidth()
            )
        }
        if (state.error != null) ErrorDialog(error = state.error)
    }
}

@Composable
private fun WkNameTextField(
    name: String,
    sendIntent: (EditWkIntent) -> Unit
) {
    BasicTextField(
        modifier = Modifier
            .clip(RoundedCornerShape(20))
            .background(secondary)
            .padding(8.dp),
        singleLine = true,
        value = name,
        textStyle = TitleTypo.copy(color = title, fontSize = 24.sp),
        onValueChange = { sendIntent(EditWkIntent.ChangeWkName(it)) }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun WkDescriptionTextField(
    modifier: Modifier = Modifier,
    description: String?,
    sendIntent: (EditWkIntent) -> Unit
) {
    OutlinedTextField(
        value = description ?: "",
        onValueChange = { sendIntent(EditWkIntent.ChangeWkDescription(it)) },
        colors = OutlinedTextFieldDefaults.colors().copy(
            focusedContainerColor = secondary,
            unfocusedContainerColor = secondary,
            focusedIndicatorColor = primary
        ),
        label = { Text(text = "Add a description...", fontSize = 14.sp) }
    )

//    val interactionSource = remember { MutableInteractionSource() }
//    BasicTextField(
//        modifier = modifier
//            .clip(RoundedCornerShape(20))
//            .fillMaxWidth(),
//        maxLines = 1,
//        value = description ?: "",
//        textStyle = TitleTypo.copy(color = title, fontSize = 24.sp),
//        interactionSource = interactionSource,
//        onValueChange = { sendIntent(EditWkIntent.ChangeWkDescription(it)) }
//    ) {
//        TextFieldDefaults.DecorationBox(
//            value = description ?: "",
//            innerTextField = {
//                Text(
//                    text = description ?: "",
//                    color = onSecondary
//                )
//            },
//            enabled = true,
//            placeholder = {
//                Text(
//                    text = "Add a description...",
//                    fontSize = 14.sp
//                )
//            },
//            singleLine = false,
//            visualTransformation = VisualTransformation.None,
//            interactionSource = interactionSource,
//            colors = TextFieldDefaults.colors().copy(
//                cursorColor = Color.White,
//                unfocusedContainerColor = secondary,
//                focusedContainerColor = secondary,
//                focusedIndicatorColor = primary,
//                unfocusedIndicatorColor = Color.Transparent,
//                focusedLabelColor = Color.Transparent,
//                unfocusedLabelColor = Color.Transparent
//            ),
//            contentPadding = PaddingValues(
//                top = 8.dp,
//                bottom = 8.dp,
//                start = 8.dp
//            )
//        )
//    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScreenPreview() {
    SalamandraTheme {
        ScreenBody(
            state = EditWkState.initial,
            sendIntent = {}
        )
    }
}
