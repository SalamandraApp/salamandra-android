package com.android.salamandra.core.boilerplate

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vzkz.fitjournal.core.boilerplate.Intent
import com.vzkz.fitjournal.core.boilerplate.State
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

private const val BUFFER_SIZE = 64

abstract class BaseViewModel<S : State, I: Intent> (initialState: S): ViewModel(){

    private val intents = MutableSharedFlow<I>(extraBufferCapacity = BUFFER_SIZE) // Intent pipeline

    var state: S by mutableStateOf(initialState)
        protected set //This makes the setValue protected so that only child classes can change it

    init {
        viewModelScope.launch {
            intents.collect { intent -> //For each action in the pipeline we execute the reduce method to update the state
                reduce(intent)
            }
        }

    }

    protected abstract fun reduce(intent: I)

    fun dispatch(intent: I) { //This is the method used to push intents to the pipeline
        val success = intents.tryEmit(intent)
        if (!success) Log.e("SLM", "MVI action buffer overflow")
    }

}