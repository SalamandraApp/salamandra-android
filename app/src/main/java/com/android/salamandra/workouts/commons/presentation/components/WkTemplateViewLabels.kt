package com.android.salamandra.workouts.commons.presentation.components

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
import com.android.salamandra.workouts.commons.presentation.constants.wkTemplateScreenConstants

@Composable
fun WkTemplateViewLabels(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        val columnLabelColor = onTertiary.copy(alpha = 0.6f)
        // EXERCISE NAME
        Text(
            modifier = Modifier
                .weight(wkTemplateScreenConstants.columnLabelWeights.exercise),
            text = stringResource(R.string.exercise),
            color = columnLabelColor,
            fontSize = 14.sp,
            style = SemiTypo
        )
        // SETS
        Text(
            modifier = Modifier
                .weight(wkTemplateScreenConstants.columnLabelWeights.sets),
            text = stringResource(R.string.sets),
            color = columnLabelColor,
            fontSize = 14.sp,
            style = SemiTypo
        )
        // REPS
        Text(
            modifier = Modifier
                .weight(wkTemplateScreenConstants.columnLabelWeights.sets),
            text = stringResource(R.string.reps),
            color = columnLabelColor,
            fontSize = 14.sp,
            style = SemiTypo
        )
        // WEIGHT
        Text(
            modifier = Modifier
                .weight(wkTemplateScreenConstants.columnLabelWeights.weight + wkTemplateScreenConstants.columnLabelWeights.button),
            text = stringResource(R.string.weight_kg),
            color = columnLabelColor,
            fontSize = 14.sp,
            style = SemiTypo
        )
    }
}
