package com.android.salamandra.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.salamandra.ui.theme.SalamandraTheme

@Composable
fun MyExerciseCardView(modifier: Modifier = Modifier, exName: String) {
    val backgroundColor = MaterialTheme.colorScheme.tertiaryContainer
    val contentColor = MaterialTheme.colorScheme.onTertiaryContainer
    Box(
        modifier = modifier
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(16.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(exName, modifier = Modifier.padding(32.dp), color = contentColor, style = MaterialTheme.typography.titleLarge)
    }
}

@Preview(showSystemUi = true)
@Composable
fun LightPreview() {
    SalamandraTheme {
        MyExerciseCardView(exName = "Bench press")
    }
}
