package com.android.salamandra.settings.presentation

import android.content.Context
import android.transition.Fade
import com.android.salamandra.R
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.sourceInformationMarkerEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.salamandra._core.domain.model.User
import com.android.salamandra._core.presentation.asUiText
import com.android.salamandra._core.presentation.components.ErrorDialog
import com.android.salamandra._core.presentation.components.FadeLip
import com.android.salamandra._core.presentation.components.shimmerEffect
import com.android.salamandra.destinations.HomeScreenDestination
import com.android.salamandra.settings.presentation.components.AccountSettingsSection
import com.android.salamandra.settings.presentation.components.UserInfoSection
import com.android.salamandra.ui.theme.NormalTypo
import com.android.salamandra.ui.theme.SalamandraTheme
import com.android.salamandra.ui.theme.TitleTypo
import com.android.salamandra.ui.theme.onSecondary
import com.android.salamandra.ui.theme.onSecondaryVariant
import com.android.salamandra.ui.theme.onTertiary
import com.android.salamandra.ui.theme.primaryVariant
import com.android.salamandra.ui.theme.secondary
import com.android.salamandra.ui.theme.subtitle
import com.android.salamandra.ui.theme.tertiary
import com.android.salamandra.ui.theme.textFieldColors
import com.android.salamandra.ui.theme.title
import com.android.salamandra.workouts.search.presentation.SearchIntent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.LocalDate

@Destination(navArgsDelegate = SettingsNavArgs::class)
@Composable
fun SettingsScreen(navigator: DestinationsNavigator, viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val events by viewModel.events.collectAsState(initial = null)
    LaunchedEffect(events) {
        when (events) {
            SettingsEvent.NavigateUp -> navigator.navigateUp()
            SettingsEvent.NavigateToHome -> navigator.navigate(HomeScreenDestination)
            null -> {}
        }
    }

    ScreenBody(
        state = state,
        sendIntent = viewModel::dispatch,
        LocalContext.current
    )
}

@Composable
private fun ScreenBody (
    state: SettingsState,
    sendIntent: (SettingsIntent) -> Unit,
    context: Context
) {

    Column (
        Modifier
            .fillMaxSize()
            .background(tertiary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsBanner(
            onExit = { sendIntent(SettingsIntent.NavigateUp) },
            onCollapseAll = { sendIntent(SettingsIntent.ChangeAllCollapse(true)) },
            onExpandAll = { sendIntent(SettingsIntent.ChangeAllCollapse(false)) },
            term = state.searchTerm,
            onChangeTerm = { sendIntent(SettingsIntent.ChangeSearchTerm(it)) }
        )
        val filteredAndSortedSections = state.sections.filter { (_, section) ->
            section.keywords.any { keywordId ->
                val keyword = context.getString(keywordId)
                keyword.contains(state.searchTerm, ignoreCase = true)
            }
        }
        LazyColumn (
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            filteredAndSortedSections.forEach { (key, section) ->
                Log.i("Settings", "$key, ${section.collapse}")
                item {
                    if (section.collapse) {
                        CollapsedSection(
                            Modifier.height(70.dp),
                            titleId = section.titleId,
                            onExpand = { sendIntent(SettingsIntent.ChangeCollapse(key, false)) }
                        )
                        HorizontalDivider(
                            Modifier.padding(horizontal = 20.dp),
                            thickness = 2.dp,
                            color = onTertiary.copy(0.3f)
                        )
                    } else {
                        ExpandedSection(
                            key = key,
                            Modifier.height(70.dp),
                            titleId = section.titleId,
                            sendIntent = sendIntent,
                            onCollapse = { sendIntent(SettingsIntent.ChangeCollapse(key, true)) }
                        )
                    }
                }
            }
        }
    }
    if (state.error != null)
        ErrorDialog(
            error = state.error.asUiText(),
            onDismiss = { sendIntent(SettingsIntent.CloseError) }
        )
}

@Composable
private fun CollapsedSection(
    modifier: Modifier = Modifier,
    titleId: Int,
    onExpand: () -> Unit
) {

    Row (
        modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onExpand() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(titleId),
            style = NormalTypo,
            fontSize = 17.sp,
            color = subtitle,
            minLines = 1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.weight(1f))

        IconButton(
            { onExpand() },){
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                tint = onTertiary,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ExpandedSection(
    key: SettingsSection,
    modifier: Modifier = Modifier,
    titleId: Int,
    onCollapse: () -> Unit,
    sendIntent: (SettingsIntent) -> Unit,
) {
    Column {
        Row(
            modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clickable { onCollapse() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(titleId),
                style = NormalTypo,
                fontSize = 17.sp,
                color = subtitle,
                minLines = 1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.weight(1f))

            IconButton(
                { onCollapse() },
                modifier = Modifier.rotate(270f)) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    tint = onTertiary,
                    contentDescription = null
                )
            }
        }
        FadeLip()
        when (key) {
            SettingsSection.Account -> AccountSettingsSection({sendIntent(SettingsIntent.Logout)})
            SettingsSection.User -> UserInfoSection(
                displayName = "DisplayName",
                username = "@username",
                birthday = LocalDate.now(),
                onSaveBirthday = {},
                onSaveDisplayName = {},
            )
        }
        FadeLip(reverse = true)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsBanner(
    modifier: Modifier = Modifier,
    term: String,
    onChangeTerm: (String) -> Unit,
    onExit: () -> Unit,
    onExpandAll: () -> Unit,
    onCollapseAll: () -> Unit,
) {


    val focusRequester = remember { FocusRequester() }
    Column (
        modifier
            .background(tertiary)
            .padding(horizontal = 20.dp, vertical = 5.dp),
    ) {
        Row (
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.clickable { onExit() },
                text = stringResource(R.string.settings),
                style = TitleTypo,
                fontSize = 20.sp,
                color = title,
                minLines = 1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.weight(1f))

            IconButton({ onCollapseAll() }) {
                Icon(
                    imageVector = Icons.Default.UnfoldLess,
                    tint = onTertiary,
                    contentDescription = null
                )
            }
            IconButton({ onExpandAll() }) {
                Icon(
                    imageVector = Icons.Default.UnfoldMore,
                    tint = onTertiary,
                    contentDescription = null
                )
            }
        }
        TextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .clip(RoundedCornerShape(50))
                .fillMaxWidth(),
            singleLine = true,
            enabled = true,
            value = term,
            placeholder = {
                Text(
                    text = stringResource(R.string.search_settings),
                    style = NormalTypo,
                    color = subtitle,
                    fontSize = 16.sp
                )
            },
            textStyle = TitleTypo.copy(fontSize = 16.sp),
            colors = textFieldColors(),
            onValueChange = { onChangeTerm(it) },
            maxLines = 1,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = subtitle
                )
            },
            trailingIcon = {
                if (!term.isEmpty()) {
                    IconButton(onClick = { onChangeTerm("") }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Clear Icon",
                            tint = subtitle
                        )
                    }
                }
            },
        )
    }
}


@Preview()
@Composable
private fun SettingsPreview() {
    SalamandraTheme {
        val updatedState = SettingsState.initial.updateSectionCollapse(SettingsSection.User, collapse = false)

        ScreenBody(
            state = updatedState,
            sendIntent = {},
            LocalContext.current
        )
    }
}

