package com.android.salamandra.domain.error

enum class PasswordError : RootError {
    TOO_SHORT,
    NO_UPPERCASE,
    NO_DIGIT
}

