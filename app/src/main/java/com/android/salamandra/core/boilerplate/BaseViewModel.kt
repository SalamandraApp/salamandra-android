package com.android.salamandra.core.boilerplate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val BUFFER_SIZE = 64

abstract class BaseViewModel<S : State, I: Intent, E: Event> (initialState: S, private val ioDispatcher: CoroutineDispatcher): ViewModel(){

    private val intents = MutableSharedFlow<I>(extraBufferCapacity = BUFFER_SIZE) // Intent pipeline

    protected val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> get() = _state.asStateFlow()

    private val _events = Channel<E?>(Channel.BUFFERED)
    val events: Flow<E?> = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            intents.collect { intent -> //For each action in the pipeline we execute the reduce method to update the state
                reduce(intent)
            }
        }

    }

    protected fun ioLaunch(block: suspend CoroutineScope.() -> Unit){
        viewModelScope.launch(ioDispatcher) { block() }
    }

    protected abstract fun reduce(intent: I)

    protected fun sendEvent(event: E){
        val success = _events.trySend(event)
        if (success.isFailure) Log.e("SLM", "Send event failed")
        else Log.e("SLM", "Event sent: ${event.toString()}")
    }

    fun dispatch(intent: I) { //This is the method used to push intents to the pipeline
        val success = intents.tryEmit(intent)
        if (!success) Log.e("SLM", "MVI action buffer overflow")
    }

}