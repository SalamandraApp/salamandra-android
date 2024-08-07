package com.android.salamandra._core.presentation.components.bottomBar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.android.salamandra.destinations.HomeScreenDestination
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.bottomBar
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.tertiary
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

@Composable
fun BottomBar(
    currentDestination: DirectionDestinationSpec,
    onClick: (DirectionDestinationSpec) -> Unit
) {
    NavigationBar(containerColor = bottomBar, contentColor = tertiary) {
        BottomBarDestinations.entries.forEach { destination ->
            NavigationBarItem(
                selected = destination.direction == currentDestination,
                onClick = {
                    onClick(destination.direction)
                },
                icon = {
                    Icon(
                        destination.icon,
                        contentDescription = stringResource(destination.label),
                        tint = onSecondary
                    )
                },
                label = {
                    Text(
                        stringResource(destination.label),
                        color = onSecondary
                    )
                },
                colors = NavigationBarItemDefaults.colors().copy(
                    selectedIndicatorColor = onSecondary.copy(alpha = 0.4f),
                )
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    SalamandraTheme {
        BottomBar(currentDestination = HomeScreenDestination) {

        }

    }
}