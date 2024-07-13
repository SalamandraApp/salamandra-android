package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.android.salamandra._core.domain.model.workout.WkTemplateElement
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.onSecondaryVariant
import com.android.salamandra.ui.theme.onTertiary

@Composable
fun WkElementComponent(
    modifier: Modifier = Modifier,
    onOption: () -> Unit,
    wkElement: WkTemplateElement,
    columnWeightVector: FloatArray,
    startPad: Dp,
    fgColor: Color
) {
    if (columnWeightVector.size != 5) {
        throw IllegalArgumentException("The length of the float array must be 5.")
    }
    val nameColor = onSecondaryVariant
    val valuesColor = onSecondaryVariant
    val valueStyle = NormalTypo.copy(
        color = valuesColor,
        textAlign = TextAlign.Center,
    )
    Row(
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(15.dp))
            .fillMaxWidth()
            .background(fgColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // EXERCISE NAME
        Text(
            modifier = Modifier
                .padding(start = startPad)
                .weight(columnWeightVector[0]),
            text = wkElement.exercise.name,
            style = SemiTypo,
            color = nameColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        val elements = listOf(
            wkElement.sets.toString(),
            wkElement.reps.toString(),
            wkElement.weight.toString()
        )
        val columnWeights = listOf(
            columnWeightVector[1],
            columnWeightVector[2],
            columnWeightVector[3]
        )

        elements.forEachIndexed { index, value ->
            Box(
                modifier = Modifier
                    .weight(columnWeights[index])
                    .padding(end = 5.dp),
            ) {

                Text(
                    text = value,
                    color = nameColor,
                    overflow = TextOverflow.Ellipsis,
                    style = valueStyle,
                    maxLines = 1,
                )
            }
        }
        Box (modifier = Modifier.weight(columnWeightVector[4])) {
            IconButton(onClick = { onOption() }) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "Move Exercise",
                    tint = onTertiary,
                )
            }
        }
    }
}
