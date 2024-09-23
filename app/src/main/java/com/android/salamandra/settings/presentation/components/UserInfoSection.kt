package com.android.salamandra.settings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra.R
import com.android.salamandra._core.presentation.components.EditDateRow
import com.android.salamandra._core.presentation.components.EditTextRow
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.subtitle
import java.time.LocalDate

@Composable
fun UserInfoSection(
    displayName: String?,
    username: String?,
    birthday: LocalDate?,
    onSaveDisplayName: (String) -> Unit,
    onSaveBirthday: (LocalDate) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .padding(end = 10.dp, start = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            EditTextRow(
                modifier = Modifier
                    .padding(bottom = 20.dp),
                text = displayName ?: stringResource(R.string.not_specified),
                onSave = { onSaveDisplayName(it) }
            )
        }
        Row(
            Modifier
                .padding(bottom = 25.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.username) + ": ",
                color = subtitle,
                style = TitleTypo,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = username ?: "-",
                color = onSecondary,
                style = TitleTypo,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.weight(1f))
        }

        EditDateRow(
            date = birthday ?: LocalDate.now(),
            onSave = { if (it != null) onSaveBirthday(it) }
        )

    }
}

@Preview
@Composable
private fun InfoSectionPreview() {
    UserInfoSection(
//        displayName = "Jaime",
        displayName = null,
        username = "vzkz",
//        username = null,
        birthday = LocalDate.now(),
        onSaveBirthday = {},
        onSaveDisplayName = {}
    )
}
