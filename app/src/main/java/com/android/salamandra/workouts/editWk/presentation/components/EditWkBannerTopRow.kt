package com.android.salamandra.workouts.editWk.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.workouts.editWk.presentation.EditWkIntent
import com.android.salamandra.workouts.editWk.presentation.EditWkState

@Composable
fun EditWkBannerTopRow (
    modifier: Modifier = Modifier,
    sendIntent: (EditWkIntent) -> Unit,
    state: EditWkState,
    middleContent: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Box(modifier = Modifier
            .clickable { sendIntent(EditWkIntent.NavigateUp) }
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                tint = onTertiary,
                contentDescription = "Search workout"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        middleContent()
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier
            .clickable { /*TODO*/ }
        ) {
            Icon(
                modifier = Modifier.size(27.dp),
                tint = primaryVariant,
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Search workout"
            )
        }
    }
}