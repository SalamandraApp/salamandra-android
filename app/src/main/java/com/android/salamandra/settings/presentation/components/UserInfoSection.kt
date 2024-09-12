package com.android.salamandra.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
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
import com.android.salamandra._core.presentation.components.AnimatedFloatingButton
import com.android.salamandra._core.presentation.components.EditDateRow
import com.android.salamandra._core.presentation.components.EditTextRow
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.subtitle
import com.android.salamandra.ui.theme.textFieldColors
import com.android.salamandra.ui.theme.title
import com.android.salamandra.workouts.editWk.presentation.components.MAX_NAME_LENGTH
import java.time.LocalDate

@Composable
fun UserInfoSection(
    displayName: String,
    username: String,
    birthday: LocalDate,
    onSaveDisplayName: (String) -> Unit,
    onSaveBirthday: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .padding(end = 10.dp, start = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EditTextRow(
            Modifier
                .padding(bottom = 10.dp),
            displayName,
            onSave = { onSaveDisplayName(it) }
        )
        Row (
            Modifier
                .padding(bottom = 25.dp),
        ) {
            Text(
                text = username,
                color = onSecondary,
                style = TitleTypo,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.weight(1f))
        }

        EditDateRow(
            date = birthday,
            onSave = {}
        )

    }
}
