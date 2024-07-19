package com.android.salamandra.workouts.commons.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.salamandra._core.domain.model.Exercise
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onSecondaryVariant
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.workouts.commons.presentation.constants.WkTemplateScreenConstants

@Composable
fun WkElementComponent(
    modifier: Modifier = Modifier,
    onOption: (Exercise) -> Unit,
    wkElement: WkTemplateElement,
    startPad: Dp,
    verticalPad: Dp = 0.dp,
    fgColor: Color,
) {

    val nameColor = onSecondaryVariant
    val valuesColor = onSecondaryVariant
    val valueStyle = NormalTypo.copy(
        color = valuesColor,
        textAlign = TextAlign.Center,
    )
    Row(
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(fgColor)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // EXERCISE NAME
        Text(
            modifier = Modifier
                .padding(start = startPad)
                .padding(vertical = verticalPad)
                .weight(WkTemplateScreenConstants.columnLabelWeights.exercise),
            text = wkElement.exercise.name,
            style = SemiTypo,
            color = nameColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        val elements = listOf(
            wkElement.sets,
            wkElement.reps,
            wkElement.weight
        )
        val columnWeights = listOf(
            WkTemplateScreenConstants.columnLabelWeights.sets,
            WkTemplateScreenConstants.columnLabelWeights.reps,
            WkTemplateScreenConstants.columnLabelWeights.weight,
        )

        elements.forEachIndexed { index, value ->
            Box(
                modifier = Modifier
                    .weight(columnWeights[index])
                    .padding(end = 5.dp),
            ) {

                Text(
                    text = value.toString(),
                    color = if (value == 0 && index != 2) colorError else nameColor,
                    overflow = TextOverflow.Ellipsis,
                    style = valueStyle,
                    maxLines = 1,
                )
            }
        }
        Box (modifier = Modifier.weight(WkTemplateScreenConstants.columnLabelWeights.button)) {
            IconButton(onClick = { onOption(wkElement.exercise) }) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "Move Exercise",
                    tint = onTertiary,
                )
            }
        }
    }
}
