package com.android.salamandra.workouts.seeWk.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra._core.presentation.components.WkTemplatePicture
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.workouts.seeWk.presentation.SeeWkIntent
import com.android.salamandra.workouts.seeWk.presentation.SeeWkState

@Composable
fun BannerTitleRow(
    modifier: Modifier = Modifier,
    sendIntent: (SeeWkIntent) -> Unit,
    state: SeeWkState
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
            val descriptionPlaceholder =
                "Really really really long description, I mean really long, why do you need so much, a little too much"
            val textToShow = state.wkTemplate.description ?: descriptionPlaceholder
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = textToShow,
                style = TitleTypo,
                fontSize = 14.sp,
                color = onTertiary,
                minLines = 3,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(1f))
            val iconColor = onTertiary.copy(0.7f)
            Row(
                modifier = Modifier.height(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = "Delete Wk",
                    tint = iconColor,
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "13 days since last time",
                    color = iconColor,
                    style = NormalTypo,
                    fontSize = 14.sp
                )
            }
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