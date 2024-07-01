package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.R

@Composable
fun WkTemplateViewLabels(
    modifier: Modifier = Modifier,
    columnWeightVector: FloatArray,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 6.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        val columnLabelColor = onTertiary.copy(alpha = 0.6f)
        // EXERCISE NAME
        Text(
            modifier = Modifier
                .weight(columnWeightVector[0])
                .padding(start = 10.dp),
            text = stringResource(R.string.exercise),
            color = columnLabelColor,
            fontSize = 14.sp,
            style = SemiTypo
        )
        // SETS
        Text(
            modifier = Modifier
                .weight(columnWeightVector[1]),
            text = stringResource(R.string.sets),
            color = columnLabelColor,
            fontSize = 14.sp,
            style = SemiTypo
        )
        // REPS
        Text(
            modifier = Modifier
                .weight(columnWeightVector[2]),
            text = stringResource(R.string.reps),
            color = columnLabelColor,
            fontSize = 14.sp,
            style = SemiTypo
        )
        // WEIGHT
        Text(
            modifier = Modifier
                .weight(columnWeightVector[3] + columnWeightVector[4]),
            text = stringResource(R.string.weight_kg),
            color = columnLabelColor,
            fontSize = 14.sp,
            style = SemiTypo
        )
    }
}
