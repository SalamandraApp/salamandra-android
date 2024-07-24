package com.android.salamandra.settings.presentation

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra.settings.domain.Repository
import com.android.salamandra.util.CoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var settingsViewModel: SettingsViewModel

    @RelaxedMockK
    private lateinit var repository: Repository

    @RelaxedMockK
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        settingsViewModel = SettingsViewModel(testDispatcher, savedStateHandle, repository)
    }

    @Test
    fun `Assert initial state`() {
        val expectedState = SettingsState(
            error = null
        )
        assert(SettingsState.initial == expectedState)
    }

}





















