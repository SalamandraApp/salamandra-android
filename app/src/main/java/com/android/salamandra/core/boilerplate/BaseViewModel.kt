package com.vzkz.fitjournal.core.boilerplate

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

private const val BUFFER_SIZE = 64

abstract class BaseViewModel<S : State, I: Intent> (initialState: S): ViewModel(){

    private val intents = MutableSharedFlow<I>(extraBufferCapacity = BUFFER_SIZE) // Intent pipeline

    var state: S by mutableStateOf(initialState)
        private set //This makes the setValue private so that it can't be modified

    init { //This code is executed when the viewmodel starts
        viewModelScope.launch {
            intents.collect { intent -> //For each action in the pipeline we execute the reduce method to update the state
                state = reduce(state, intent)
            }
        }

    }

    abstract fun reduce(state: S, intent: I): S

    fun dispatch(intent: I) { //This is the method used to push intents to the pipeline
        val success = intents.tryEmit(intent)
        if (!success) Log.e("Jaime", "MVI action buffer overflow")
    }

}