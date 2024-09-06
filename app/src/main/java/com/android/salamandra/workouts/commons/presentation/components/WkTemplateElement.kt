package com.android.salamandra.workouts.commons.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra._core.domain.model.workout.template.WkTemplateElement
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onSecondaryVariant
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.workouts.commons.presentation.constants.WkTemplateScreenConstants

@Composable
fun WkTemplateElement(
    modifier: Modifier = Modifier,
    onOption: (Int) -> Unit,
    wkElement: WkTemplateElement,
    fgColor: Color,
    editable: Boolean = true
) {

    val nameColor = onSecondaryVariant
    val valuesColor = onSecondaryVariant
    val valueStyle = NormalTypo.copy(
        color = valuesColor,
        textAlign = TextAlign.Center,
    )

    val icon = @Composable{
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = "Move Exercise",
            tint = onTertiary,
        )
    }
    Row(
        modifier = modifier
            .clickable { if (!editable) onOption(0) }
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(fgColor)
            .padding(horizontal = 10.dp)
            .then(if (!editable) Modifier.padding(vertical = 15.dp) else Modifier)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val exerciseNameModifier = Modifier.weight(WkTemplateScreenConstants.columnLabelWeights.exercise).padding(end = 5.dp)
        // EXERCISE NAME
        if (editable) {
            TextButton (
                shape = RoundedCornerShape(10.dp),
                modifier = exerciseNameModifier,
                onClick = { onOption(0) },
                contentPadding = PaddingValues(0.dp),
                content = {
                    Text(
                        text = wkElement.exercise.name,
                        style = SemiTypo,
                        fontSize = 15.sp,
                        color = nameColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.weight(1f))
                }
            )
        } else {
            Box (exerciseNameModifier) {
                Text(
                    text = wkElement.exercise.name,
                    style = SemiTypo,
                    fontSize = 15.sp,
                    color = nameColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Left
                )
            }
        }

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
                    .padding(start = 2.dp, end = 5.dp),
            ) {
                val text = @Composable {
                    Text(
                        modifier = Modifier.fillMaxSize(),
                        text = value.toString(),
                        color = if (value == 0 && index != 2) colorError else nameColor,
                        overflow = TextOverflow.Ellipsis,
                        style = valueStyle,
                        maxLines = 1,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Left
                    )
                }
                if (editable) {
                    TextButton(
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .fillMaxSize(),
                        onClick = { onOption(index + 1) },
                        content = {
                            text()
                            Spacer(Modifier.weight(1f))
                        }
                    )

                } else { text() }
            }
        }
        Box (modifier = Modifier.weight(WkTemplateScreenConstants.columnLabelWeights.button)) {
            if (editable) {
                IconButton(onClick = { if (editable) onOption(0) }) {
                    icon()
                }
            } else {
                icon()
            }
        }
    }
}
