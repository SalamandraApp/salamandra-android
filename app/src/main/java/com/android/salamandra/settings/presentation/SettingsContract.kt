package com.android.salamandra.settings.presentation

import com.android.salamandra.R
import com.android.salamandra._core.boilerplate.Event
import com.android.salamandra._core.boilerplate.Intent
import com.android.salamandra._core.boilerplate.NavArgs
import com.android.salamandra._core.boilerplate.State
import com.android.salamandra._core.domain.error.RootError
import java.time.LocalDate


data class SettingsState(
    val error: RootError?,
    val searchTerm: String = "",
    val sections: Map<SettingsSection, Section> = emptyMap(),
    val username: String?,
    val displayName: String?,
    val dateOfBirth: LocalDate?
) : State {
    companion object {
        val initial = SettingsState(
            error = null,
            sections = mapOf(
                SettingsSection.User to Section(
                    titleId = R.string.user_info,
                    keywords = listOf(R.string.username, R.string.display_name, R.string.birthday)
                ),
                SettingsSection.Account to Section(
                    titleId = R.string.account_settings,
                    keywords = listOf(R.string.logout,)
                )
            ),
            username = null,
            displayName = null,
            dateOfBirth = null
        )
    }
    fun updateSectionCollapse(sectionId: SettingsSection, collapse: Boolean): SettingsState {
        val updatedSections = sections.mapValues { (key, section) ->
            if (key == sectionId) {
                if (collapse) section.copy(collapse = true) else section.copy(collapse = false)
            } else section
        }
        return this.copy(sections = updatedSections)
    }
    fun updateAllSectionsCollapse(collapse: Boolean): SettingsState {
        val updatedSections = sections.mapValues { (_, section) ->
            section.copy(collapse = collapse)
        }
        return this.copy(sections = updatedSections)
    }
}

enum class SettingsSection {
    User,
    Account
}
data class Section(
    var collapse: Boolean = true,
    val titleId: Int,
    val keywords: List<Int> = emptyList()
)

sealed class SettingsIntent: Intent {
    data class Error(val error: RootError): SettingsIntent()

    data object CloseError: SettingsIntent()

    data object NavigateUp: SettingsIntent()

    data object Logout: SettingsIntent()

    data class ChangeCollapse(val section: SettingsSection, val state: Boolean): SettingsIntent()

    data class ChangeAllCollapse(val state: Boolean): SettingsIntent()

    data class ChangeSearchTerm(val newTerm: String): SettingsIntent()

    data class SaveDisplayName(val newDisplayName: String?): SettingsIntent()

    data class SaveBirthday(val newDateOfBirth: LocalDate?): SettingsIntent()
}

sealed class SettingsEvent: Event{
    data object NavigateUp: SettingsEvent()

    data object NavigateToHome: SettingsEvent()
}

data class SettingsNavArgs(
    val dummy: Int? = null
): NavArgs