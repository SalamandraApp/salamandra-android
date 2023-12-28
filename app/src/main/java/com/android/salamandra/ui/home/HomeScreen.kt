package com.android.salamandra.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.salamandra.R
import com.android.salamandra.ui.components.MyExerciseCardView
import com.android.salamandra.ui.components.MyColumn
import com.android.salamandra.ui.components.MySpacer
import com.android.salamandra.ui.theme.SalamandraTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator) {
    ScreenBody()
}

@Composable
private fun ScreenBody() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        MyColumn(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = "",
                onValueChange = { },
                label = {
                    Text(
                        text = stringResource(R.string.search_by_name),
                        fontSize = 16.sp
                    )
                },
                shape = CircleShape,
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.padding(8.dp)) {
                        Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
                    }
                }
            )
            LazyColumn{
                item {
                    MyExerciseCardView(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        exName = "Bench press"
                    )
                    MyExerciseCardView(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        exName = "Pull ups"
                    )
                    MyExerciseCardView(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        exName = "Shoulder press"
                    )
                    MyExerciseCardView(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        exName = "Squat"
                    )
                }
            }
        }
    }
}



@Preview
@Composable
fun LightPreview() {
    SalamandraTheme {
        ScreenBody()
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun DarkPreview() {
//    SalamandraTheme {
//        ScreenBody()
//    }
//}