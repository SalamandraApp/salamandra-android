package com.android.salamandra.workouts.seeWk.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.workouts.seeWk.presentation.SeeWkIntent
import com.android.salamandra.workouts.seeWk.presentation.SeeWkState

@Composable
fun SeeWkBannerTopRow(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onExecuteWk: () -> Unit,
    middleContent: @Composable () -> Unit,
    executeButton: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Box(modifier = Modifier
            .clickable { onGoBack() }
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIosNew,
                tint = onTertiary,
                contentDescription = "Search workout"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        middleContent()
        Spacer(modifier = Modifier.weight(1f))
        if (executeButton)
            FloatingActionButton(
                modifier = Modifier
                    .padding(vertical = 8.dp),
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