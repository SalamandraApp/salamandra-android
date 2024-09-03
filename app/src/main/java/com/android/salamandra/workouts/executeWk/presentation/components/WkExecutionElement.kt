package com.android.salamandra.workouts.executeWk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra.R
import com.android.salamandra._core.domain.model.workout.executions.WkExecutionElement
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.onSecondaryVariant
import com.android.salamandra.ui.theme.primary
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.secondaryVariant
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title

data class UiState(
    val height: Dp,
    val stringSize: TextUnit,
    val smallStringSize: TextUnit,
    val stringColor: Color,
    val borderColor: Color,
    val icon: ImageVector,
    val iconSize: Dp
)


@Composable
fun WkExecutionElement(
    modifier: Modifier = Modifier,
    activeIndex: Int,
    currentIndex: Int,
    element: WkExecutionElement
) {
    val uiState = when {
    currentIndex > activeIndex -> UiState(50.dp, 20.sp, 15.sp, title, onSecondaryVariant, Icons.Outlined.PlayArrow, 30.dp)
    currentIndex < activeIndex -> UiState(50.dp, 20.sp, 15.sp, onSecondaryVariant, secondaryVariant, Icons.Outlined.Check, 30.dp)
    else -> UiState(75.dp, 26.sp, 20.sp, title, primary, Icons.Outlined.PlayArrow, 35.dp)
}
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, primary, RoundedCornerShape(20.dp))
            .height(uiState.height),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (element.weight != null) {
            Row(
                Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(15.dp))
                Icon(
                    modifier = Modifier
                        .size(uiState.iconSize),
                    imageVector = Icons.Outlined.PlayArrow,
                    tint = primary,
                    contentDescription = null
                )

                Spacer(Modifier.weight(1f))
                Text(
                    text = element.reps.toString(),
                    color = uiState.stringColor,
                    style = TitleTypo,
                    fontSize = uiState.stringSize
                )
            }

            Icon(
                modifier = Modifier.padding(horizontal = 20.dp),
                imageVector = Icons.Outlined.Close,
                tint = uiState.stringColor,
                contentDescription = null
            )
            val annotatedString = buildAnnotatedString {
                append(
                    if (element.weight % 1 == 0.0) {
                        element.weight.toInt().toString()
                    } else {
                        element.weight.toString()
                    }
                )
                withStyle(
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = uiState.smallStringSize,
                        color = uiState.stringColor,
                        fontWeight = FontWeight.Normal
                    ).toSpanStyle()
                ) {
                    append(" kg")
                }
            }
            Box(
                Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = annotatedString,
                    color = title,
                    style = TitleTypo,
                    fontSize = 26.sp
                )
            }
        }
        else {

            Row(
                Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(15.dp))
                Icon(
                    modifier = Modifier
                        .size(35.dp),
                    imageVector = Icons.Outlined.PlayArrow,
                    tint = primary,
                    contentDescription = null
                )
            }


            val annotatedString = buildAnnotatedString {
                append(element.reps.toString())
                withStyle(
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = uiState.smallStringSize,
                        color = title,
                        fontWeight = FontWeight.Normal
                    ).toSpanStyle()
                ) {
                    append("  reps")
                }
            }
            Text(
                text = annotatedString,
                color = title,
                style = TitleTypo,
                fontSize = 26.sp
            )

            Spacer(Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun WkExecutionElementPreview() {
    Box(modifier = Modifier.fillMaxSize().background(tertiary)) {
        LazyColumn {
            item{
                WkExecutionElement(
                    modifier = Modifier.padding(20.dp),
                    activeIndex = 1,
                    currentIndex = 0,
                    element = WkExecutionElement(
                        id = "",
                        setNumber = 1,
                        reps = 15,
                        weight = null,
                        rest = 120,
                    )
                )

            }
            item{
                WkExecutionElement(
                    modifier = Modifier.padding(20.dp),
                    activeIndex = 1,
                    currentIndex = 0,
                    element = WkExecutionElement(
                        id = "",
                        setNumber = 1,
                        reps = 15,
                        weight = 85.0,
                        rest = 120,
                    )
                )
            }

            item{
                WkExecutionElement(
                    modifier = Modifier.padding(20.dp),
                    activeIndex = 0,
                    currentIndex = 0,
                    element = WkExecutionElement(
                        id = "",
                        setNumber = 1,
                        reps = 15,
                        weight = null,
                        rest = 120,
                    )
                )

            }
            item{
                WkExecutionElement(
                    modifier = Modifier.padding(20.dp),
                    activeIndex = 0,
                    currentIndex = 0,
                    element = WkExecutionElement(
                        id = "",
                        setNumber = 1,
                        reps = 15,
                        weight = 85.0,
                        rest = 120,
                    )
                )
            }
            item{
                WkExecutionElement(
                    modifier = Modifier.padding(20.dp),
                    activeIndex = 0,
                    currentIndex = 1,
                    element = WkExecutionElement(
                        id = "",
                        setNumber = 1,
                        reps = 15,
                        weight = null,
                        rest = 120,
                    )
                )

            }
            item{
                WkExecutionElement(
                    modifier = Modifier.padding(20.dp),
                    activeIndex = 0,
                    currentIndex = 1,
                    element = WkExecutionElement(
                        id = "",
                        setNumber = 1,
                        reps = 15,
                        weight = 85.0,
                        rest = 120,
                    )
                )
            }

        }
    }
}