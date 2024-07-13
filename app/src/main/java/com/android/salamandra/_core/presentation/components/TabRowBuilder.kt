package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.tertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabRowBuilder(
    contents: List<@Composable () -> Unit>,
    icons: List<ImageVector>,
    titles: List<String>
) {
    require(contents.size == icons.size && icons.size == titles.size) {
        "All lists must have the same length"
    }

    val selectedTabIndex = remember { mutableStateOf(0) }
    val selectedColor = primaryVariant.copy(0.7f)
    val unselectedColor = onTertiary.copy(0.7f)

    PrimaryTabRow(
        containerColor = tertiary,
        selectedTabIndex = selectedTabIndex.value,
        indicator = {
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedTabIndex.value, matchContentSize = true),
            )
        },

        divider = {},
        tabs = {
            titles.forEachIndexed { index, title ->
                Tab(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .clip(RoundedCornerShape(30)),
                    selectedContentColor = selectedColor,
                    unselectedContentColor = unselectedColor,
                    selected = selectedTabIndex.value == index,
                    onClick = { selectedTabIndex.value = index },
                    icon = {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = null
                        )
                    },
                    text = { Text(text = title) }
                )
            }
        }
    )
    Spacer(modifier = Modifier.height(20.dp))
    contents[selectedTabIndex.value]()
}