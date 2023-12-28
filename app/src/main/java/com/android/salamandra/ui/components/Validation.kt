package com.android.salamandra.ui.components



fun isValidCredentials(email: String, password: String): Boolean {
    val emailPattern = validateEmail(email)
    val passwordPattern = Regex("^(?=.*[0–9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
    return emailPattern && passwordPattern.matches(password)
}


fun validateEmail(email: String): Boolean =
    email.matches(Regex("[a-zA-Z0-9.+_-]+@[a-z]+\\.+[a-z]+"))

fun validatePassword(password: String) =
    validateMinimum(password) && validateCapitalizedLetter(password)

private fun validateCapitalizedLetter(password: String): Boolean =
    password.matches(Regex(".*[A-Z].*"))

private fun validateMinimum(password: String): Boolean =
    password.length > 6
