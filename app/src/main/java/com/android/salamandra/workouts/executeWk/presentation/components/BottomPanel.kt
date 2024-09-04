package com.android.salamandra.workouts.executeWk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.FastForward
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra.R
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.onSecondaryVariant
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.secondaryVariant
import com.android.salamandra.ui.theme.subtitle
import com.android.salamandra.ui.theme.tertiary

@Composable
fun BottomPanel(
    modifier: Modifier = Modifier,
    onFinish: () -> Unit,
    onEdit: () -> Unit,
    onSkipSet: () -> Unit,
    onSkipExercise: () -> Unit,
    onNote: () -> Unit
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 20.dp),
    ) {

        Row (
            Modifier
                .fillMaxHeight()
                .weight(1.2f)
                .padding(end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingActionButton (
                modifier = Modifier.size(45.dp),
                containerColor = secondaryVariant.copy(0.8f),
                contentColor = onSecondaryVariant,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                onClick = { onNote() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Notes,
                    contentDescription = null,
                )
            }
            Spacer(Modifier.weight(1f))
            FloatingActionButton (
                modifier = Modifier.size(45.dp),
                containerColor = secondaryVariant.copy(0.8f),
                contentColor = onSecondaryVariant,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                onClick = { onEdit() }) {
                Icon(
                    imageVector = Icons.Outlined.Edit ,
                    contentDescription = null,
                )
            }
        }
        Column (Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally){
            Spacer(Modifier.weight(1f))
            FloatingActionButton (
                modifier = Modifier.size(70.dp),
                containerColor = primaryVariant.copy(0.3f),
                contentColor = primaryVariant,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                onClick = { onFinish() }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                )
            }
            Spacer(Modifier.weight(1f))
        }

        Column (
            Modifier
                .weight(1.2f)
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            val skipFontSize = 12.sp
            val skipHorizontalPad = 5.dp
            Row (
                modifier = Modifier
                    .border(2.dp, secondaryVariant, RoundedCornerShape(40))
                    .clickable { onSkipSet() }
                    .padding(horizontal = skipHorizontalPad, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.SkipNext,
                    contentDescription = null,
                    tint = subtitle,
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "Skip Set",
                    color = subtitle,
                    style = SemiTypo,
                    fontSize = skipFontSize
                )
            }
            Spacer(Modifier.weight(1f))
            Row (
                modifier = Modifier
                    .border(2.dp, secondaryVariant, RoundedCornerShape(40))
                    .clickable { onSkipExercise() }
                    .padding(horizontal = skipHorizontalPad, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.FastForward,
                    contentDescription = null,
                    tint = subtitle,
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "Skip Exercise",
                    color = subtitle,
                    style = SemiTypo,
                    fontSize = skipFontSize
                )
            }

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
            onEdit = {},
            onFinish = {},
            onSkipSet = {},
            onSkipExercise = {},
            onNote = {}
        )
    }
}