package com.android.salamandra._core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.salamandra.workouts.commons.presentation.components.WkTemplateViewLabels
import com.android.salamandra.workouts.commons.presentation.constants.WkTemplateScreenConstants

@Composable
fun WkTemplateFixBanner(
    modifier: Modifier = Modifier,
    middleContent: @Composable () -> Unit,
    onGoBack: () -> Unit,
    onActionButton: () -> Unit,
    actionIcon: ImageVector
) {

    val dpSideMargin = WkTemplateScreenConstants.outsideMargin
    val dpButtons = WkTemplateScreenConstants.bannerRowHeights.buttons
    val dpLabels = WkTemplateScreenConstants.bannerRowHeights.labels
    val dpMargins= WkTemplateScreenConstants.bannerRowHeights.margins
    Column(
        modifier = modifier
            .clickable {  }
            .fillMaxWidth()
            .padding(horizontal = dpSideMargin)
            .padding(top = dpSideMargin),
    ) {
        WkTemplateTopRow(
            modifier = Modifier.height(dpButtons),
            onGoBack = onGoBack,
            onActionButton = onActionButton,
            middleContent = middleContent,
            showActionButton = true,
            actionIcon = actionIcon,
        )
        Spacer(Modifier.height(dpMargins[3]))
        WkTemplateViewLabels(
            modifier = Modifier.height(dpLabels)
        )
    }
}

@Preview
@Composable
private fun FixedBannerPreview() {
    WkTemplateFixBanner(
        modifier = Modifier.height(500.dp),
        onActionButton = {},
        onGoBack = {},
        middleContent = {},
        actionIcon = Icons.Default.SatelliteAlt
    )
}
