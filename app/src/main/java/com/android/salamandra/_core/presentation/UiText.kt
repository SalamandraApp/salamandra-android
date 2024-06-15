package com.android.salamandra._core.presentation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

sealed class UiText {
    data class DynamicString(val value: String): UiText()

    class StringResource(
        @StringRes val id: Int,
        val args: Array<Any> = arrayOf()
    ): UiText()

    @Composable
    fun asString(): String {
        return when (this){
            is DynamicString -> value
            is StringResource -> LocalContext.current.getString(id, *args)
        }
    }

//    fun asString(context: Context): String{ //I think this is not needed (this app is full compose)
//        return when(this){
//            is DynamicString -> value
//            is StringResource -> context.getString(id, *args)
//        }
//    }
}