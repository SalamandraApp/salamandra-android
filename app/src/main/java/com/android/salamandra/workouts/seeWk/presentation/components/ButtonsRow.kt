package com.android.salamandra.workouts.seeWk.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.workouts.seeWk.presentation.SeeWkIntent
import com.android.salamandra.workouts.seeWk.presentation.SeeWkState

@Composable
fun ButtonsRow (
    modifier: Modifier = Modifier,
    onEdit: () -> Unit,
    onShare: () -> Unit,
    onStats: () -> Unit,
    onExecuteWk: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    )
    {

        Box(modifier = Modifier
            .clickable { onEdit() }
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                tint = onTertiary,
                contentDescription = "Edit workout"
            )
        }
        Box(
            modifier = Modifier
                .padding(start = 20.dp)
                .clickable { onShare() }
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                tint = onTertiary,
                contentDescription = "Share workout"
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ExtendedFloatingActionButton(
            containerColor = secondary,
            contentColor = primaryVariant,
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            onClick = { onStats() }) {
            Icon(
                imageVector = Icons.Outlined.QueryStats,
                contentDescription = "Wk Stats",
            )
            Text(
                text = "Stats",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        FloatingActionButton(
            modifier = Modifier.padding(start = 30.dp),
            containerColor = primaryVariant.copy(0.3f),
            contentColor = primaryVariant,
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            onClick = { onExecuteWk() }) {
            Icon(
                imageVector = Icons.Filled.PlayCircle,
                contentDescription = "Execute Workout",
            )
        }
    }
}