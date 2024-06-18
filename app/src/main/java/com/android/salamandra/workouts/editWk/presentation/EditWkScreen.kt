package com.android.salamandra.workouts.editWk.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.DragIndicator
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.presentation.components.MyColumn
import com.android.salamandra._core.presentation.components.MyRow
import com.android.salamandra._core.presentation.components.MySpacer
import com.android.salamandra._core.presentation.components.WkPlaceholder
import com.android.salamandra._core.util.EXERCISE_LIST
import com.android.salamandra._core.util.LONG_EXERCISE_LIST
import com.android.salamandra._core.util.WORKOUT_TEMPLATE
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.WkTemplateElementTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onPrimary
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.onTertiary
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
                        WkNameTextField(
                            state.wkTemplate.name,
                            !state.showSearchExercise,
                            sendIntent
                        )
                        MySpacer(size = 4)
                        WkDescriptionTextField(
                            modifier = Modifier.weight(1f),
                            state.wkTemplate.description,
                            !state.showSearchExercise,
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
                    MyRow(modifier = Modifier.clickable {
                        if (!state.showSearchExercise) {/*TODO*/
                        }
                    }) {
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
                        enabled = !state.showSearchExercise,
                        onClick = { sendIntent(EditWkIntent.ShowSearchExercise(true)) }) {
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
                    IconButton(onClick = { /*TODO*/ }, enabled = !state.showSearchExercise) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = "Finish",
                            tint = primary
                        )
                    }
                }
                MySpacer(size = 8)
                val weightSeparator = Modifier.weight(0.2f)
                Row(Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(0.3f))
                    Text(text = stringResource(R.string.exercise), color = onSecondary)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = stringResource(R.string.reps), color = onSecondary)
                    Spacer(modifier = weightSeparator)
                    Text(text = stringResource(R.string.sets), color = onSecondary)
                    Spacer(modifier = weightSeparator)
                    Text(text = stringResource(R.string.weight), color = onSecondary)
                    MySpacer(size = 2)
                    Text(
                        modifier = Modifier.align(Alignment.Bottom),
                        text = "(Kg)",
                        color = onSecondary,
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.weight(0.2f))

                }

            }

            LazyColumn(
                modifier = Modifier
                    .background(secondary)
                    .weight(2.8f)
                    .fillMaxWidth()
            ) {
                items(state.wkTemplate.elements) {
                    MySpacer(size = 8)
                    WkElementComponent(wkElement = it)
                }
            }
        }

        if (state.showSearchExercise) SearchScreen(state, sendIntent)

        if (state.error != null)
            ErrorDialog(
                error = state.error.asUiText(),
                onDismiss = { sendIntent(EditWkIntent.CloseError) }
            )
    }
}

@Composable
private fun SearchScreen(
    state: EditWkState,
    sendIntent: (EditWkIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding()
            .background(secondary.copy(alpha = 0.6f)),
        contentAlignment = Alignment.TopCenter
    ) {
        val roundedCorner = 20.dp
        MyColumn(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 12.dp)
                .clip(RoundedCornerShape(roundedCorner))
                .fillMaxWidth()
                .background(tertiary),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Type an exercise to search...") },
                value = state.searchTerm,
                shape = RoundedCornerShape(roundedCorner),
                onValueChange = { sendIntent(EditWkIntent.ChangeSearchTerm(it)) },
                trailingIcon = {
                    IconButton(onClick = { sendIntent(EditWkIntent.SearchExercise) }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search exercise"
                        )
                    }
                }
            )
            LazyColumn {
                items(state.exerciseList) {
                    SearchResultElement(exercise = it)
                }
            }

        }
        IconButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
                .clip(
                    RoundedCornerShape(30)
                )
                .background(primary),
            onClick = { sendIntent(EditWkIntent.ShowSearchExercise(false)) }
        ) {
            Icon(
                imageVector = Icons.Outlined.Done,
                tint = onPrimary,
                contentDescription = "Done"
            )
        }
    }
}

@Composable
private fun SearchResultElement(modifier: Modifier = Modifier, exercise: Exercise) {
    MyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(20))
            .background(secondary)
            .padding(horizontal = 8.dp)
    ) {
        Text(text = exercise.name, color = onSecondary, fontSize = 18.sp)
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Exercise info",
                tint = onSecondary
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Add exercise",
                tint = onSecondary
            )

        }
    }

}

@Composable
private fun WkElementComponent(modifier: Modifier = Modifier, wkElement: WkTemplateElement) {
    MyRow(
        modifier = modifier
            .padding(horizontal = 6.dp)
            .clip(RoundedCornerShape(15))
            .background(tertiary)
            .padding(4.dp)
    ) {
        val weightSeparator = Modifier.weight(0.4f)
        Text(
            modifier = Modifier.padding(start = 2.dp),
            text = wkElement.position.toString(),
            color = onTertiary
        )
        Spacer(modifier = Modifier.weight(0.2f))
        Text(
            modifier = Modifier.widthIn(min = 94.dp, max = 94.dp),
            text = wkElement.exercise.name,
            style = WkTemplateElementTypo
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.widthIn(min = 20.dp, max = 20.dp),
            text = wkElement.reps.toString(),
            style = WkTemplateElementTypo
        )
        Spacer(modifier = weightSeparator)
        Text(
            modifier = Modifier.widthIn(min = 20.dp, max = 20.dp),
            text = wkElement.sets.toString(),
            style = WkTemplateElementTypo
        )
        Spacer(modifier = weightSeparator)
        Text(
            modifier = Modifier.widthIn(min = 38.dp, max = 38.dp),
            text = wkElement.weight.toString(),
            style = WkTemplateElementTypo
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Outlined.DragIndicator,
                contentDescription = "Move Exercise",
                tint = onTertiary
            )
        }


    }

}

@Composable
private fun WkNameTextField(
    name: String,
    editEnabled: Boolean,
    sendIntent: (EditWkIntent) -> Unit
) {
    BasicTextField(
        modifier = Modifier
            .clip(RoundedCornerShape(20))
            .background(secondary)
            .padding(8.dp),
        singleLine = true,
        enabled = editEnabled,
        value = name,
        textStyle = TitleTypo.copy(color = title, fontSize = 24.sp),
        onValueChange = { sendIntent(EditWkIntent.ChangeWkName(it)) }
    )
}

@Composable
private fun WkDescriptionTextField(
    modifier: Modifier = Modifier,
    description: String?,
    editEnabled: Boolean,
    sendIntent: (EditWkIntent) -> Unit
) {
    OutlinedTextField(
        value = description ?: "",
        onValueChange = { sendIntent(EditWkIntent.ChangeWkDescription(it)) },
        enabled = editEnabled,
        colors = OutlinedTextFieldDefaults.colors().copy(
            focusedContainerColor = secondary,
            unfocusedContainerColor = secondary,
            focusedIndicatorColor = primary
        ),
        label = { Text(text = "Add a description...", fontSize = 14.sp) }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScreenPreview() {
    SalamandraTheme {
        ScreenBody(
            state = EditWkState.initial.copy(
                wkTemplate = WORKOUT_TEMPLATE,
                showSearchExercise = true,
                exerciseList = EXERCISE_LIST
            ),
            sendIntent = {}
        )
    }
}
