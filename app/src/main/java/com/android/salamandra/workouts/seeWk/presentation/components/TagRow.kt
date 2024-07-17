package com.android.salamandra.workouts.seeWk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.workouts.seeWk.presentation.SeeWkIntent
import com.android.salamandra.workouts.seeWk.presentation.SeeWkState

@Composable
fun TagRow(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(end = 15.dp)
        ) {
            val bgColor = onTertiary.copy(0.4f)
            val items = listOf("#mytag1", "#tag2", "#thistag3", "#tag4")
            LazyRow {
                items(items.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = if (index == items.size - 1) 0.dp else 10.dp)
                            .background(bgColor, shape = RoundedCornerShape(50)),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            text = items[index],
                            color = tertiary,
                            style = SemiTypo,
                            fontSize = 13.sp
                        )

                    }
                }
            }
            if (items.size >= 5)
                FadeLip(vertical = true, modifier = Modifier.align(Alignment.CenterEnd))
        }

    }
}