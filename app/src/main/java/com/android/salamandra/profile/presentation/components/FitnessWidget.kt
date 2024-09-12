package com.android.salamandra.profile.presentation.components

import android.media.Image
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Pending
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra._core.domain.model.enums.FitnessGoal
import com.android.salamandra._core.domain.model.enums.FitnessLevel
import com.android.salamandra._core.domain.model.enums.getIcon
import com.android.salamandra._core.domain.model.enums.toFitnessGoal
import com.android.salamandra._core.domain.model.enums.toFitnessLevel
import com.android.salamandra._core.domain.model.enums.toInt
import com.android.salamandra._core.presentation.components.AnimatedFloatingButton
import com.android.salamandra._core.presentation.components.GradientSlider
import com.android.salamandra.ui.theme.SemiTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.colorConfirm
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.tertiary

@Composable
fun fitnessWdiget (
    textColor: Color,
    iconColor: Color,
    editFitness: String,
    onSaveFitness: () -> Unit,
    onFitness: (String) -> Unit,
    onEditFitness: (String, FitnessLevel?, FitnessGoal?) -> Unit,
    newLevel: FitnessLevel?,
    newGoal: FitnessGoal?,
    fitnessLevel: FitnessLevel?,
    fitnessGoal: FitnessGoal?,
) {
    val weightTop = if (editFitness == "level") 1.1f else if (editFitness == "goal") 0.9f else 1f
    val weightBottom = 2f - weightTop
    val level = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 16.sp)) {
            append("Fitness Level: ")
        }
        withStyle(style = SpanStyle(color = textColor.copy(alpha = 0.5f))) {
            append(fitnessLevel?.toString() ?: "???")
        }
    }
    val goal = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 16.sp)) {
            append("Fitness Goal: ")
        }
        withStyle(style = SpanStyle(color = textColor.copy(alpha = 0.5f))) {
            append(fitnessGoal?.toString() ?: "???")
        }
    }
    Column (Modifier.padding(horizontal = 10.dp)) {
        val modifierLevel = Modifier
            .weight(weightTop)
            .then(if (editFitness != "level") Modifier.clickable { onFitness("level") } else Modifier)
        val editableLevel = buildAnnotatedString {
            append("Fitness Level: ")
            withStyle(style = SpanStyle(color = textColor.copy(alpha = 0.5f))) {
                append(newLevel?.toString() ?: "???")
            }
        }
        val sliderPositionLevel = remember { mutableFloatStateOf(newLevel?.toInt()?.toFloat() ?: 0f) }
        if (editFitness == "level") {
            editable(
                modifier = Modifier,
                verticalAlignment = Alignment.Top,
                icon = newLevel?.getIcon(),
                iconColor = iconColor,
                textColor = textColor,
                onSave = onSaveFitness,
                text = editableLevel,
                slider = {
                    GradientSlider(
                        thumbColor = iconColor,
                        maxValue = FitnessLevel.getSize() - 1,
                        steps = FitnessLevel.getSize() - 2,
                        gradientColors = listOf(primaryVariant, colorError),
                        sliderPosition = sliderPositionLevel.value,
                        onChangeValue = {
                            sliderPositionLevel.value = it
                            onEditFitness("level", it.toInt().toFitnessLevel(), null)
                        }
                    )
                }
            )
        } else {
            inactive(
                modifier = modifierLevel,
                text = level,
                textColor = textColor,
                iconColor = iconColor,
                icon = fitnessLevel?.getIcon() ?: Icons.Outlined.Pending,
            )
        }
        val modifierGoal = Modifier
            .weight(weightBottom)
            .then(if (editFitness != "goal") Modifier.clickable { onFitness("goal") } else Modifier)
        val editableGoal = buildAnnotatedString {
            append("Fitness Goal: ")
            withStyle(style = SpanStyle(color = textColor.copy(alpha = 0.5f))) {
                append(newGoal?.toString() ?: "???")
            }
        }
        // Spacer(Modifier.weight(1f))
        val sliderPositionGoal =
            remember { mutableFloatStateOf(newGoal?.toInt()?.toFloat() ?: 0f) }
        if (editFitness == "goal") {
            editable(
                modifier = Modifier,
                verticalAlignment = Alignment.Bottom,
                icon = newGoal?.getIcon(),
                iconColor = iconColor,
                textColor = textColor,
                onSave = onSaveFitness,
                text = editableGoal,
                slider = {
                    GradientSlider(
                        thumbColor = iconColor,
                        maxValue = FitnessGoal.getSize() - 1,
                        steps = FitnessGoal.getSize() - 2,
                        gradientColors = listOf(primaryVariant, colorError),
                        sliderPosition = sliderPositionGoal.value,
                        onChangeValue = {
                            sliderPositionGoal.value = it
                            onEditFitness("goal", null, it.toInt().toFitnessGoal())
                        }
                    )
                }
            )
        } else {
            inactive(
                modifier = modifierGoal,
                text = goal,
                textColor = textColor,
                iconColor = iconColor,
                icon = fitnessGoal?.getIcon() ?: Icons.Outlined.Pending,
            )
        }
    }
}

@Composable
private fun editable (
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical,
    slider: @Composable () -> Unit,
    textColor: Color,
    iconColor: Color,
    text: AnnotatedString,
    icon: ImageVector?,
    onSave: () -> Unit,
) {
    val iconToShow = icon ?: Icons.Outlined.Pending
    Row (modifier, verticalAlignment = verticalAlignment) {  }
    Column (Modifier, verticalArrangement = Arrangement.Center) {
        Row (verticalAlignment = Alignment.CenterVertically){
            Text(
                modifier = Modifier.weight(1f),
                text = text,
                color = textColor,
                style = SemiTypo,
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Left
            )
            Icon(
                modifier = Modifier.size(25.dp),
                imageVector = iconToShow,
                contentDescription = null,
                tint = iconColor
            )
            Spacer(Modifier.width(20.dp))
            AnimatedFloatingButton(
                modifier = Modifier.size(50.dp),
                initialIcon = Icons.Filled.CheckCircle,
                pressedIcon = Icons.Filled.CheckCircle,
                delayTime = 700,
                initialContainerColor = onSecondary.copy(0.2f),
                pressedContainerColor = primaryVariant.copy(0.3f),
                initialIconColor = onSecondary,
                pressedIconColor = primaryVariant,
                onPress = {onSave()},
                instant = false
            )

        }
        slider()
    }
}

@Composable
private fun inactive(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    textColor: Color,
    icon: ImageVector,
    iconColor: Color,
) {
    Row (modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            color = textColor,
            style = SemiTypo,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.weight(1f))
        Icon(
            modifier = Modifier.size(25.dp),
            imageVector = icon,
            contentDescription = null,
            tint = iconColor
        )
    }
}
