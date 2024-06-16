package com.android.salamandra.workouts.editWk.presentation

import com.android.salamandra._core.boilerplate.BaseViewModel
import com.android.salamandra._core.presentation.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class EditWkViewModel @Inject constructor(ioDispatcher: CoroutineDispatcher) :
    BaseViewModel<EditWkState, EditWkIntent, EditWkEvent>(EditWkState.initial, ioDispatcher) {

    override fun reduce(intent: EditWkIntent) {
        when (intent) {
            is EditWkIntent.Error -> onError(intent.error)
            is EditWkIntent.CloseError -> onCloseError()
            is EditWkIntent.ChangeWkName -> _state.update {
                it.copy(
                    wkTemplate = it.wkTemplate.copy(
                        name = intent.newName
                    )
                )
            }

            is EditWkIntent.ChangeWkDescription ->
                if (intent.newDescription != "") _state.update {
                    it.copy(wkTemplate = it.wkTemplate.copy(description = intent.newDescription))
                }
                else _state.update { it.copy(wkTemplate = it.wkTemplate.copy(description = null)) }
        }
    }

    private fun onError(error: UiText) = _state.update { it.copy(error = error) }
    private fun onCloseError() = _state.update { it.copy(error = null) }
}