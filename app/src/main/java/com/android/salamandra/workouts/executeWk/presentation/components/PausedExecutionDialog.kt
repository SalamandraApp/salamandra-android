package com.android.salamandra.workouts.executeWk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlaylistAddCheckCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.android.salamandra.R
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.colorConfirm
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.onSecondaryVariant
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.secondaryVariant
import com.android.salamandra.ui.theme.tertiary

@Composable
fun PausedExecutionDialog(
    modifier: Modifier = Modifier,
    ableToRecord: Boolean = true,
    onRecord: () -> Unit,
    onDiscard: () -> Unit,
    onExit: () -> Unit
) {
    Dialog(
        onDismissRequest = { onExit() },
    ) {
        val cardWidth = if (ableToRecord) 310.dp else 250.dp
        Card(
            modifier = Modifier
                .width(cardWidth)
                .height(150.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors().copy(
                containerColor = secondary
            )
        ) {
            Column (
                Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row (verticalAlignment = Alignment.CenterVertically){
                    Row(Modifier.weight(1f)) {
                        Spacer(Modifier.width(5.dp))
                        IconButton(
                            onClick = { onExit() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = onSecondary.copy(0.8f)
                            )
                        }
                    }
                    Text(
                        text = stringResource(R.string.workout_paused),
                        modifier = Modifier.padding(start = 8.dp),
                        color = onSecondary,
                        style = TitleTypo,
                        fontSize = 20.sp
                    )

                    Spacer(Modifier.weight(1f))
                }
                Row(
                    Modifier
                        .padding(horizontal = 15.dp)
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!ableToRecord)
                        Spacer(Modifier.weight(1f))

                    ExtendedFloatingActionButton(
                        containerColor = tertiary.copy(0.8f),
                        contentColor = colorError,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp),
                        onClick = { onDiscard() }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = null,
                        )
                        Text(
                            text = stringResource(R.string.discard),
                            modifier = Modifier.padding(start = 8.dp),
                            style = TitleTypo,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    if (ableToRecord) {
                        ExtendedFloatingActionButton(
                            containerColor = primaryVariant.copy(0.3f),
                            contentColor = primaryVariant,
                            elevation = FloatingActionButtonDefaults.elevation(0.dp),
                            onClick = { onRecord() }) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                            )
                            Text(
                                text = stringResource(R.string.record),
                                modifier = Modifier.padding(start = 8.dp),
                                style = TitleTypo,
                                fontSize = 18.sp
                            )
                        }
                    }

                }
            }
        }
    }
}

@Preview
@Composable
private fun PausedWorkoutDialogPreview() {
    Box(Modifier.fillMaxSize().background(tertiary)) {
        PausedExecutionDialog(
            onRecord = {},
            onDiscard = {},
            onExit = {}
        )
    }
}