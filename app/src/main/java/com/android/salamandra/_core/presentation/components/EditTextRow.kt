package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EditOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra.R
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.subtitle
import com.android.salamandra.ui.theme.textFieldColors
import com.android.salamandra.ui.theme.title
import com.android.salamandra.workouts.editWk.presentation.components.MAX_NAME_LENGTH

@Composable
fun EditTextRow(
    modifier: Modifier = Modifier,
    text: String,
    onSave: (String) -> Unit,
    editable: Boolean = true,
    maxLength: Int = MAX_NAME_LENGTH,
) {
    val newText = remember { mutableStateOf("") }
    val editState = remember { mutableStateOf(false) }
    Row (
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = stringResource(R.string.display_name) + ": ",
            color = subtitle,
            style = TitleTypo,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (!editState.value || !editable) {
            Text(
                text = text,
                color = onSecondary,
                style = TitleTypo,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.weight(1f))
            Icon(
                modifier = Modifier.clickable {
                    editState.value = true
                    newText.value = text
                },
                tint = if (editable) onSecondary else onSecondary.copy(0.5f),
                imageVector = if (editable) Icons.Outlined.Edit else Icons.Outlined.EditOff,
                contentDescription = null
            )
        } else {
            TextField(
                modifier = Modifier.clip(RoundedCornerShape(10.dp)).weight(.8f),
                singleLine = true,
                enabled = true,
                placeholder = { Text(text = "Display Name", style = TitleTypo, color = onTertiary, fontSize = 16.sp) },
                value = newText.value,
                textStyle = TitleTypo.copy(fontSize = 16.sp),
                colors = textFieldColors(),
                onValueChange = { if (it.length < maxLength) {newText.value = it} }
            )
            AnimatedFloatingButton(
                modifier = Modifier.weight(0.2f).padding(start = 10.dp),
                initialIcon = Icons.Filled.CheckCircle,
                pressedIcon = Icons.Filled.CheckCircle,
                delayTime = 400,
                initialContainerColor = onSecondary.copy(0.2f),
                pressedContainerColor = primaryVariant.copy(0.3f),
                initialIconColor = onSecondary,
                pressedIconColor = primaryVariant,
                onPress = {
                    editState.value = false
                    onSave(newText.value)
                },
                instant = false
            )
        }
    }
}
