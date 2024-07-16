package com.android.salamandra.workouts.editWk.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.workouts.editWk.presentation.EditWkIntent
import com.android.salamandra.workouts.editWk.presentation.EditWkState

@Composable
fun EditTagRow(
    modifier: Modifier = Modifier,
    sendIntent: (EditWkIntent) -> Unit,
    state: EditWkState
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val wTags = 800f
        val wButton = 200f
        Box(
            modifier = Modifier
                .weight(wTags)
                .padding(end = 15.dp)
        ) {
            val borderColor = onTertiary.copy(0.8f)
            val items = listOf("#mytag1", "#tag2", "#thistag3", "#tag4")
            LazyRow {
                items(items.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = if (index == items.size - 1) 0.dp else 10.dp)
                            .border(1.dp, borderColor, shape = RoundedCornerShape(50)),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .clickable { }
                        ) {
                            Icon(
                                modifier = Modifier.size(15.dp),
                                imageVector = Icons.Filled.Close,
                                tint = onTertiary,
                                contentDescription = "Leading Icon"
                            )
                        }
                        Text(
                            text = items[index],
                            color = onTertiary,
                            style = SemiTypo,
                            fontSize = 13.sp
                        )
                        Box(modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .clickable { }
                        ) {
                            Icon(
                                modifier = Modifier.size(15.dp),
                                imageVector = Icons.Filled.Edit,
                                tint = onTertiary,
                                contentDescription = "Trailing Icon"
                            )
                        }
                    }
                }
            }
            if (items.size >= 3)
                FadeLip(vertical = true, modifier = Modifier.align(Alignment.CenterEnd))
        }
        FloatingActionButton(
            modifier = Modifier.weight(wButton),
            containerColor = secondary,
            contentColor = primaryVariant,
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Add Exercise",
            )
        }
    }
}