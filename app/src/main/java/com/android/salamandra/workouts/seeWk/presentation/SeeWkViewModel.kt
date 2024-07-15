package com.android.salamandra.workouts.seeWk.presentation

import androidx.lifecycle.SavedStateHandle
import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra._core.domain.model.workout.WorkoutTemplate
import com.android.salamandra.navArgs
import com.android.salamandra.workouts.commons.domain.WorkoutsRepository
import com.android.salamandra.workouts.seeWk.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SeeWkViewModel @Inject constructor(
    ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
    private val repository: Repository,
    private val workoutsRepository: WorkoutsRepository
) :
    BaseViewModel<SeeWkState, SeeWkIntent, SeeWkEvent>(SeeWkState.initial, ioDispatcher) {

    override fun reduce(intent: SeeWkIntent) {
        when (intent) {
            is SeeWkIntent.Error -> _state.update { it.copy(error = intent.error) }
            is SeeWkIntent.CloseError -> _state.update { it.copy(error = null) }
            is SeeWkIntent.NavigateUp -> sendEvent(SeeWkEvent.NavigateUp)
            SeeWkIntent.ShowBottomSheet -> _state.update { it.copy(bottomSheet = true) }
            SeeWkIntent.HideBottomSheet -> _state.update { it.copy(bottomSheet = false) }
        }
    }

    init {
        val navArgs: SeeWkNavArgs = savedStateHandle.navArgs()
        ioLaunch {
            when(val wkToSee = repository.getWkTemplate(workoutId = navArgs.wkTemplateId)){
                is Result.Success -> {
                    _state.update { it.copy(wkTemplate = wkToSee.data) }
                    workoutsRepository.storeWkTemplateInLocal(wkTemplate = wkToSee.data)
                }
                is Result.Error -> _state.update { it.copy(error = wkToSee.error) }
            }
        }
    }
}