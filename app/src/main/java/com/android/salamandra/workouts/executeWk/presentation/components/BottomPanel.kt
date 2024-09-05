package com.android.salamandra.workouts.executeWk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.MoreTime
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.StopCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onSecondaryVariant
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondaryVariant
import com.android.salamandra.ui.theme.tertiary

@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    onFinishSet: () -> Unit,
    onSkipSet: () -> Unit,
    onAddRest: () -> Unit,
    onStop: () -> Unit,
    paused: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 20.dp),
    ) {

        Row(
            Modifier
                .fillMaxHeight()
                .weight(1.2f)
                .padding(end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingActionButton(
                modifier = Modifier.size(45.dp),
                containerColor = secondaryVariant.copy(0.8f),
                contentColor = colorError,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                onClick = { onStop() }) {
                Icon(
                    imageVector = if (paused) Icons.Outlined.Pause else Icons.Outlined.StopCircle,
                    contentDescription = null,
                )
            }
            Spacer(Modifier.weight(1f))
            FloatingActionButton(
                modifier = Modifier.size(45.dp),
                containerColor = secondaryVariant.copy(0.8f),
                contentColor = onSecondaryVariant,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                onClick = { onAddRest() }) {
                Icon(
                    imageVector = Icons.Outlined.MoreTime,
                    contentDescription = null,
                )
            }


        }
        Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.weight(1f))
            FloatingActionButton(
                modifier = Modifier.size(70.dp),
                containerColor = primaryVariant.copy(0.3f),
                contentColor = primaryVariant,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                onClick = { onFinishSet() }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                )
            }
            Spacer(Modifier.weight(1f))
        }


        Row(
            Modifier
                .fillMaxHeight()
                .weight(1.2f)
                .padding(end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(Modifier.weight(1f))
            FloatingActionButton(
                modifier = Modifier.width(85.dp),
                containerColor = secondaryVariant.copy(0.8f),
                contentColor = onSecondaryVariant,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                onClick = { onSkipSet() }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Filled.SkipNext,
                    contentDescription = null,
                )
            }
            Spacer(Modifier.weight(1f))

        }
    }
}

@Preview
@Composable
private fun BottomPanelPreview() {
    Column (Modifier.fillMaxSize().background(tertiary)) {
        Spacer(Modifier.weight(1f))
        FadeLip()
        BottomPanel(
            modifier = Modifier.height(130.dp),
            onFinishSet = {},
            onSkipSet = {},
            onAddRest = {},
            onStop = {}
        )
    }
}