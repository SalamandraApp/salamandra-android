package com.android.salamandra.workouts.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra.destinations.EditWkScreenDestination
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.tertiary
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

@Composable
private fun ScreenBody(
    state: SearchState,
    sendIntent: (SearchIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(tertiary)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(8.dp),
            value = state.searchTerm,
            onValueChange = {
                sendIntent(SearchIntent.ChangeSearchTerm(it))
            },
            placeholder = { Text(text = stringResource(R.string.type_an_exercise_to_search)) },
            trailingIcon = {
                IconButton(onClick = {
                    sendIntent(SearchIntent.SearchExercise)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search exercise"
                    )
                }
            },
            shape = CircleShape
        )

        LazyColumn(
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = 90.dp)
        ) {
            items(state.searchResultExercises) {
                SearchExerciseElement(
                    exercise = it,
                    onAddExercise = { exercise ->
                        sendIntent(SearchIntent.AddExercise(exercise.exId))
                    }
                )
                Spacer(Modifier.size(8.dp))

            }
        }

        Button(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = { sendIntent(SearchIntent.NavigateToEdit) }) {
            Text("Done", color = tertiary)
        }


        if (state.error != null)
            ErrorDialog(
                error = state.error.asUiText(),
                onDismiss = { sendIntent(SearchIntent.CloseError) }
            )

    }
}

@Composable
private fun SearchExerciseElement(
    modifier: Modifier = Modifier,
    exercise: Exercise,
    onAddExercise: (Exercise) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .padding(horizontal = 8.dp)
            .background(secondary)
            .padding(12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1f))
        Text(exercise.name, color = onSecondary)
        Spacer(Modifier.weight(1f))
        IconButton(onClick = { onAddExercise(exercise) }) {
            Icon(Icons.Outlined.Add, contentDescription = "Add exercise", tint = onSecondary)
        }

    }
}

@Preview()
@Composable
private fun ScreenPreview() {
    SalamandraTheme {
        ScreenBody(
            state = SearchState.initial,
            sendIntent = {}
        )
    }
}
