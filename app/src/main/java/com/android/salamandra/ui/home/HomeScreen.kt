package com.android.salamandra.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra.R
import com.android.salamandra.destinations.LoginScreenDestination
import com.android.salamandra.ui.components.MyColumn
import com.android.salamandra.ui.theme.SalamandraTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator, viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        when (events) {
            else -> {}
        }
    }

    ScreenBody(
        state = state,
        sendIntent = viewModel::dispatch,
        onLogout = {
            viewModel.onLogout()
            navigator.navigate(LoginScreenDestination)
        }
    )
}

@Composable
private fun ScreenBody(
    state: HomeState,
    sendIntent: (HomeIntent) -> Unit,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        var term by remember { mutableStateOf("") }

        MyColumn(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = term,
                onValueChange = { term = it },
                label = {
                    Text(
                        text = stringResource(R.string.search_by_name),
                        fontSize = 16.sp
                    )
                },
                shape = CircleShape,
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { sendIntent(HomeIntent.SearchExercise(term)) }, modifier = Modifier.padding(8.dp)) {
                        Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
                    }
                }
            )
            if (state.exList != null) {
                LazyColumn {
                    items(state.exList) {
//                        MyExerciseCardView(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 8.dp),
//                            exName = it.name
//                        )
                    }
                }

            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { onLogout() }, modifier = Modifier.padding(8.dp)) {
                Text(text = "Log out")
            }
        }

    }
}


@Preview
@Composable
fun LightPreview() {
    SalamandraTheme {
        ScreenBody(
            state = HomeState.initial,
            sendIntent = {},
            onLogout = {}
        )
    }
}