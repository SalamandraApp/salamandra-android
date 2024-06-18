package com.android.salamandra.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.presentation.components.MyColumn
import com.android.salamandra._core.presentation.components.MyRow
import com.android.salamandra.ui.theme.onPrimary
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.primary
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.workouts.editWk.presentation.EditWkIntent
import com.android.salamandra.workouts.editWk.presentation.EditWkState

@Composable
fun SearchScreen(
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
                items(state.exerciseList) { exercise ->
                    SearchResultElement(
                        exercise = exercise,
                        onAddExercise = { sendIntent(EditWkIntent.AddExerciseToTemplate(exercise)) })
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
fun SearchResultElement(
    modifier: Modifier = Modifier,
    exercise: Exercise,
    onAddExercise: () -> Unit
) {
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
        IconButton(onClick = onAddExercise) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Add exercise",
                tint = onSecondary
            )

        }
    }

}