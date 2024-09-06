package com.android.salamandra._core.presentation.components

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant

@Composable
fun WkTemplateTopRow(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onActionButton: () -> Unit,
    middleContent: @Composable () -> Unit = {},
    showActionButton: Boolean = false,
    actionIcon: ImageVector?
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
        Row (modifier = Modifier.weight(1f)) {
            Spacer(modifier = Modifier.weight(1f))
            if (showActionButton && actionIcon != null) {
                FloatingActionButton(
                    containerColor = primaryVariant.copy(0.3f),
                    contentColor = primaryVariant,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
                    onClick = { onActionButton() }) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}