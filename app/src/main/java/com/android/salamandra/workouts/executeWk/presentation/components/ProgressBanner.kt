package com.android.salamandra.workouts.executeWk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.title

@Composable
fun ProgressBanner(
    modifier: Modifier = Modifier,
    size: Int,
    current: Int,
    exerciseName: String,
) {

    Column (
        modifier = modifier
            .padding(horizontal = 20.dp)
    ) {

        // Navigator
        Row (
            modifier = Modifier.weight(.8f)
        ) { }
        ProgressBar(
            modifier = Modifier
                .height(15.dp),
            size = size,
            current = current
        )
        Row (
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.weight(1f))
            Text(
                text = exerciseName,
                color = title,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )
            Spacer(Modifier.weight(1f))
        }

    }
}

@Preview
@Composable
private fun ProgressBannerPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        ProgressBanner(
            modifier = Modifier
                .height(130.dp)
                .background(tertiary),
            size = 5,
            current = 2,
            exerciseName = "Exercise Name"
        )
    }
}