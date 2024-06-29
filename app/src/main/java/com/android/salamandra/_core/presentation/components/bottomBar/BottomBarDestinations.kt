package com.android.salamandra._core.presentation.components.bottomBar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.android.salamandra.R
import com.android.salamandra.destinations.DirectionDestination
import com.android.salamandra.destinations.HomeScreenDestination
import com.android.salamandra.destinations.ProfileScreenDestination

enum class BottomBarDestinations(
    val direction: DirectionDestination,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    HomeScreen(
        direction = HomeScreenDestination,
        icon = Icons.Outlined.Home,
        label = R.string.home
    ),
    ProfileScreen(
        direction = ProfileScreenDestination,
        icon = Icons.Outlined.Person,
        label = R.string.profile
    ),
}