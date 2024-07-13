package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.colorMessage
import com.android.salamandra.ui.theme.primary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.title

@Composable
fun ExerciseInfo(

) {

    Text(
        text = "Example Exercise Name",
        style = TitleTypo,
        fontSize = 20.sp,
        color = title,
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(4) {
            Icon(Icons.Filled.MilitaryTech, contentDescription = null, tint = primaryVariant)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(4) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "Muscle:", color = Color.White)
                Text(text = "1", color = Color.Gray)
            }
        }
    }
}