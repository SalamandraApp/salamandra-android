package com.android.salamandra._core.domain.error

enum class PasswordError : RootError {
    TOO_SHORT,
    NO_UPPERCASE,
    NO_DIGIT,
    NO_SPECIAL_CHARACTER
}

