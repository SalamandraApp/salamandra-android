package com.android.salamandra.workouts.executeWk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Timer
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
import com.android.salamandra._core.domain.model.enums.TimeIntervalFormat
import com.android.salamandra._core.domain.model.workout.executions.WkExecutionElement
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.onSecondaryVariant
import com.android.salamandra.ui.theme.primary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.secondaryVariant
import com.android.salamandra.ui.theme.subtitle
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title

data class UiState(
    val bottomPadding: Dp,
    val height: Dp,
    val stringSize: TextUnit,
    val smallStringSize: TextUnit,
    val stringColor: Color,
    val borderColor: Color,
    val icon: ImageVector,
    val iconSize: Dp,
    val iconColor: Color,
    val restStringSize: TextUnit? = null,
    val restColor: Color? = null,
    val topPadding: Dp = 0.dp
)


@Composable
fun WkExecutionElement(
    modifier: Modifier = Modifier,
    activeIndex: Int,
    currentIndex: Int,
    element: WkExecutionElement,
    onClick: () -> Unit
) {
    val noWeight = element.weight == null
    val uiState = when {
        currentIndex > activeIndex -> UiState(
            bottomPadding = 25.dp,
            height = 60.dp,
            stringSize = 20.sp,
            smallStringSize = 17.sp,
            stringColor = title,
            borderColor = onSecondaryVariant,
            icon = Icons.Default.PlayArrow,
            iconSize = 25.dp,
            iconColor = Color.Transparent,
            restStringSize = 18.sp,
            restColor = onSecondary.copy(0.8f)
        )
        currentIndex < activeIndex -> UiState(
            bottomPadding = 30.dp,
            height = 50.dp,
            stringSize = 20.sp,
            smallStringSize = 17.sp,
            stringColor = onSecondaryVariant,
            borderColor = secondaryVariant,
            icon = Icons.Outlined.Check,
            iconSize = 25.dp,
            iconColor = secondaryVariant,
            restStringSize = null,
            restColor = null
        )
        else -> UiState(
            bottomPadding = 30.dp,
            height = 75.dp,
            stringSize = 26.sp,
            smallStringSize = 20.sp,
            stringColor = title,
            borderColor = primaryVariant,
            icon = Icons.Outlined.PlayArrow,
            iconSize = 35.dp,
            iconColor = primary,
            restStringSize = 21.sp,
            restColor = onSecondaryVariant,
            topPadding = 15.dp
        )
    }

    Column (
        modifier = modifier.padding(bottom = uiState.bottomPadding, top = uiState.topPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, uiState.borderColor, RoundedCornerShape(20.dp))
                .clickable { onClick() }
                .height(uiState.height),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (Modifier.width(50.dp)) {
                Spacer(Modifier.width(15.dp))
                Icon(
                    modifier = Modifier
                        .size(uiState.iconSize),
                    imageVector = uiState.icon,
                    tint = uiState.iconColor,
                    contentDescription = null
                )
            }
            Box (
                Modifier.weight(1f),
                contentAlignment = if (noWeight) Alignment.Center else Alignment.CenterEnd
            ) {

                val annotatedString = buildAnnotatedString {
                    append(element.reps.toString())
                    withStyle(
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = uiState.smallStringSize,
                            color = uiState.stringColor,
                            fontWeight = FontWeight.Normal
                        ).toSpanStyle()
                    ) {
                        append("  reps")
                    }
                }
                if (noWeight) {
                    Text(
                        text = annotatedString,
                        color = uiState.stringColor,
                        style = TitleTypo,
                        fontSize = uiState.stringSize
                    )
                } else {
                    Text(
                        text = element.reps.toString(),
                        color = uiState.stringColor,
                        style = TitleTypo,
                        fontSize = uiState.stringSize
                    )
                }
            }
            if (!noWeight) {
                Icon(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    imageVector = Icons.Outlined.Close,
                    tint = uiState.stringColor,
                    contentDescription = null
                )

                val annotatedString = buildAnnotatedString {
                    append(
                        if (element.weight!! % 1 == 0.0) {
                            element.weight!!.toInt().toString()
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
                        color = uiState.stringColor,
                        style = TitleTypo,
                        fontSize = uiState.stringSize
                    )
                }

            }
            Row (Modifier.width(50.dp)) {
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    tint = uiState.borderColor.copy(0.6f),
                    contentDescription = null
                )
                Spacer(Modifier.width(15.dp))
            }
        }

        if (uiState.restStringSize != null && uiState.restColor != null) {
            Row (
                Modifier.padding(top = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    tint = uiState.restColor,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(start = 3.dp),
                    text = element.rest.toString(),
                    color = uiState.restColor,
                    style = NormalTypo,
                    fontSize = uiState.restStringSize
                )
            }
        }
    }
}

@Preview
@Composable
private fun WkExecutionElementPreview() {
    Box(modifier = Modifier.fillMaxSize().background(tertiary)) {
        val modifier = Modifier.padding(horizontal = 20.dp)
        LazyColumn {
            item{
                WkExecutionElement(
                    modifier = modifier.padding(top = 20.dp),
                    activeIndex = 1,
                    currentIndex = 0,
                    element = WkExecutionElement(
                        id = "",
                        setNumber = 1,
                        reps = 15,
                        weight = 85.0,
                        rest = 120,
                    ),
                    onClick = {}
                )

            }
        }
    }
}