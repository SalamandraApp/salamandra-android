package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra.R
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.colorError
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.secondary

@Composable
fun NotImplented() {
    Column (
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(horizontal = 15.dp)
    ) {
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = stringResource(R.string.not_implemented),
            style = TitleTypo,
            fontSize = 20.sp,
            color = colorError,
            minLines = 1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = stringResource(R.string.working_on_it),
            style = TitleTypo,
            fontSize = 18.sp,
            color = onTertiary,
            minLines = 1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Row (
            modifier = Modifier.padding(top = 20.dp)
        ){
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = Icons.Outlined.Construction,
                tint = colorError,
                contentDescription = null
            )
            Spacer(modifier = Modifier.weight(1f))
        }


    }
}