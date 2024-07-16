package com.android.salamandra.workouts.editWk.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra.R
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.workouts.editWk.presentation.EditWkIntent
import com.android.salamandra.workouts.editWk.presentation.EditWkState

@Composable
fun ButtonsRowBanner (
    modifier: Modifier = Modifier,
    state: EditWkState,
    sendIntent: (EditWkIntent) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = "Delete Wk",
            tint = colorError,
        )
        Text(
            modifier = Modifier.padding(start = 5.dp),
            text = "Delete",
            color = colorError,
            style = SemiTypo,
            fontSize = 14.sp
        )
        IconButton(
            onClick = { sendIntent(EditWkIntent.ShowBottomSheet(0)) },
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.FolderOpen,
                tint = onTertiary,
                contentDescription = "Folder workout"
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ExtendedFloatingActionButton(
            containerColor = primaryVariant.copy(0.3f),
            contentColor = primaryVariant,
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            onClick = { sendIntent(EditWkIntent.NavigateToSearch) }) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Add Exercise",
            )
            Text(
                text = stringResource(R.string.add_exercise),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}