package com.android.salamandra.workouts.seeWk.presentation

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.navArgs
import com.android.salamandra.workouts.commons.domain.WorkoutsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SeeWkViewModel @Inject constructor(
    ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
    private val workoutsRepository: WorkoutsRepository
) :
    BaseViewModel<SeeWkState, SeeWkIntent, SeeWkEvent>(SeeWkState.initial, ioDispatcher) {

    override fun reduce(intent: SeeWkIntent) {
        when (intent) {
            is SeeWkIntent.Error -> _state.update { it.copy(error = intent.error) }

            is SeeWkIntent.CloseError -> _state.update { it.copy(error = null) }

            is SeeWkIntent.NavigateUp -> sendEvent(SeeWkEvent.NavigateUp)

            is SeeWkIntent.ShowExerciseInfo -> _state.update { it.copy(selectedElementIndex = intent.index) }
            is SeeWkIntent.ShowTemplateInfo-> _state.update { it.copy(bottomSheetTab = intent.tab) }
            SeeWkIntent.ShowNotImplementedBanner-> _state.update { it.copy(notImplementedBanner = true) }

            SeeWkIntent.HideBottomSheet -> _state.update { it.copy(selectedElementIndex = null, bottomSheetTab = null, notImplementedBanner = false) }

            SeeWkIntent.StartWk -> sendEvent(SeeWkEvent.StartWk)
        }
    }

    init {
        val navArgs: SeeWkNavArgs = savedStateHandle.navArgs()
        ioLaunch {
            when (val wkToSee = workoutsRepository.getWkTemplate(workoutId = navArgs.wkTemplateId)) {
                is Result.Success -> {
                    _state.update { it.copy(wkTemplate = wkToSee.data) }
                    workoutsRepository.storeWkTemplateInLocal(wkTemplate = wkToSee.data)
                }

                is Result.Error -> _state.update { it.copy(error = wkToSee.error) }
            }
        }
    }
}