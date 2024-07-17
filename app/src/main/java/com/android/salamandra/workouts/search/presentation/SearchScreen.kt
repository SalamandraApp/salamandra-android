package com.android.salamandra.workouts.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.BottomSheet
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.presentation.components.ExerciseInfo
import com.android.salamandra.destinations.EditWkScreenDestination
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.colorMessage
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.subtitle
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.textFieldColors
import com.android.salamandra.ui.theme.title
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(navArgsDelegate = SearchNavArgs::class)
@Composable
fun SearchScreen(navigator: DestinationsNavigator, viewModel: SearchViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        when (events) {
            SearchEvent.NavigateToEdit -> navigator.navigate(EditWkScreenDestination(addedExercises = state.addedExercisesIds))
            null -> {}
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
    state: SearchState,
    sendIntent: (SearchIntent) -> Unit
) {
    val active = remember { mutableStateOf(false) }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(tertiary)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .height(30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box (modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier.clickable { sendIntent(SearchIntent.NavigateToEdit) }
                ) {
                    Icon(
                        modifier = Modifier.padding(start = 10.dp),
                        imageVector = Icons.Outlined.ArrowBackIosNew,
                        tint = onTertiary,
                        contentDescription = "Exit search exercise"
                    )
                }
            }
            Text(
                text = stringResource(R.string.search_exercise),
                color = onTertiary,
                fontSize = 18.sp,
                style = TitleTypo,
            )

            Spacer(modifier = Modifier.weight(1f))
        }
        Row (
            modifier = Modifier.weight(1f)
        )
        {
            SearchBar(
                modifier = Modifier
                    .padding()
                    .clip(RoundedCornerShape(30.dp))
                    .fillMaxWidth(),
                colors = SearchBarDefaults.colors(
                    containerColor = secondary,
                    inputFieldColors = textFieldColors(),
                    dividerColor = secondary,
                ),
                query = state.searchTerm,
                onQueryChange = { sendIntent(SearchIntent.ChangeSearchTerm(it)) },
                onSearch = { sendIntent(SearchIntent.SearchExercise) },
                placeholder = {
                    Text(
                        stringResource(R.string.type_exercise),
                        overflow = TextOverflow.Ellipsis,
                        color = subtitle
                    )
                },
                active = active.value,
                onActiveChange = { active.value = !active.value },
                shape = RoundedCornerShape(40.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        tint = subtitle
                    )
                },
                trailingIcon = {
                    if (state.searchTerm.isNotBlank()) {
                        IconButton(onClick = { sendIntent(SearchIntent.ChangeSearchTerm("")) }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Clear Icon",
                                tint = subtitle
                            )
                        }
                    }
                },
                content = {
                    LazyColumn {
                        val size = state.searchResultExercises.size
                        itemsIndexed(state.searchResultExercises) { index, exercise ->
                            SearchExerciseComponent(
                                exercise = exercise,
                                onAddExercise = { sendIntent(SearchIntent.AddExercise(exercise.exId)) },
                                onExerciseInfo = { sendIntent(SearchIntent.ShowBottomSheet(it)) },
                                last = index == size - 1,
                            )

                        }
                    }

                }
            )
        }
        if (state.bottomSheet && state.selectedExercise != null) {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = false,
            )
            BottomSheet (
                sheetState = sheetState,
                onDismiss = {sendIntent(SearchIntent.HideBottomSheet)},
                content = { ExerciseInfo(state.selectedExercise) }
            )
        }
        if (state.error != null)
            ErrorDialog(
                error = state.error.asUiText(),
                onDismiss = { sendIntent(SearchIntent.CloseError) }
            )

    }
}

@Composable
private fun SearchExerciseComponent(
    modifier: Modifier = Modifier,
    exercise: Exercise,
    onAddExercise: (Exercise) -> Unit,
    onExerciseInfo: (Exercise) -> Unit,
    last: Boolean
) {
    Row(
        modifier = modifier
            .padding(start = 20.dp, end = 5.dp)
            .height(60.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = exercise.name,
            color = title,
            style = TitleTypo,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(10.dp),
            text = stringResource(exercise.mainMuscleGroup.stringId),
            color = onTertiary,
            style = SemiTypo
        )
        IconButton(onClick = { onExerciseInfo(exercise) }) {
            Icon(
                Icons.Outlined.Info,
                contentDescription = "Info exercise",
                tint = onTertiary
            )
        }
        IconButton(onClick = { onAddExercise(exercise) }) {
            Icon(
                Icons.Outlined.AddCircle,
                contentDescription = "Add exercise",
                tint = primaryVariant
            )
        }

    }
    if (!last) {
        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            color = tertiary.copy(0.8f),
            thickness = 2.dp
        )
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    SalamandraTheme {
        ScreenBody(
            state = SearchState.initial,
            sendIntent = {}
        )
    }
}
