package com.android.salamandra.workouts.editWk.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra._core.presentation.components.WkTemplatePicture
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.subtitle
import com.android.salamandra.ui.theme.textFieldColors
import com.android.salamandra.workouts.editWk.presentation.EditWkIntent
import com.android.salamandra.workouts.editWk.presentation.EditWkState

@Composable
fun BannerTitleRow(
    modifier: Modifier = Modifier,
    wkName: String,
    wkDescription: String?,
    onChangeName: (String) -> Unit,
    onChangeDescription: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(end = 20.dp)
        ) {
            TextField(
                modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                singleLine = true,
                enabled = true,
                value = wkName,
                textStyle = TitleTypo.copy(fontSize = 16.sp),
                colors = textFieldColors(),
                onValueChange = { onChangeName(it) }
            )
            Spacer(modifier = Modifier.weight(1f))

            val textToShow = wkDescription ?: ""
            TextField(
                modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                enabled = true,
                value = textToShow,
                placeholder = {
                    if (textToShow.isEmpty()) {
                        Text(text = "Description...", style = TitleTypo, color = onTertiary, fontSize = 12.sp)
                    }
                },
                minLines = 2,
                maxLines = 2,
                textStyle = TitleTypo.copy(fontSize = 12.sp),
                colors = textFieldColors(),
                onValueChange = { onChangeDescription(it) },
            )

        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            WkTemplatePicture(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                shape = RoundedCornerShape(5)
            )
        }
    }
}