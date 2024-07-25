package com.android.salamandra._core.domain

import com.android.salamandra._core.domain.error.PasswordError
import com.android.salamandra._core.domain.error.Result
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class UserDataValidatorTest{

    private val userDataValidator: UserDataValidator = UserDataValidator()

    @Test
    fun `When password is shorter than 9, too short error`()= runTest {
        // Arrange
        val expectedError = PasswordError.TOO_SHORT
        val password = "shortPas"
        // Act
        val result = userDataValidator.validatePassword(password)
        // Assert
        assert(result is Result.Error && result.error == expectedError)

    }
    
    @Test
    fun `When password doesn't have uppercase, no uppercase error`() = runTest {
        // Arrange
        val expectedError = PasswordError.NO_UPPERCASE
        val password = "long_enough_no_uppercase"
        // Act
        val result = userDataValidator.validatePassword(password)
        // Assert
        assert(result is Result.Error && result.error == expectedError)
    }

    @Test
    fun `When password doesn't have a digit, no digit error`() = runTest {
        // Arrange
        val expectedError = PasswordError.NO_DIGIT
        val password = "long_enough_Uppercase_no_digit"
        // Act
        val result = userDataValidator.validatePassword(password)
        // Assert
        assert(result is Result.Error && result.error == expectedError)
    }

    @Test
    fun `When password doesn't have a special character, no special character error`() = runTest {
        // Arrange
        val expectedError = PasswordError.NO_SPECIAL_CHARACTER
        val password = "longEnoughUppercaseDigit9NoSpecialCharacter"
        // Act
        val result = userDataValidator.validatePassword(password)
        // Assert
        assert(result is Result.Error && result.error == expectedError)
    }

    @Test
    fun `Different special characters work`() = runTest {
        // Arrange
        val password = "longEnoughUppercaseDigit9NoSpecialCharacter"
        // Act and assert
        var result = userDataValidator.validatePassword("$password:")
        assert(result is Result.Success)
        result = userDataValidator.validatePassword("$password#")
        assert(result is Result.Success)
        result = userDataValidator.validatePassword("$password&")
        assert(result is Result.Success)
        result = userDataValidator.validatePassword("$password$")
        assert(result is Result.Success)
        result = userDataValidator.validatePassword("$password-")
        assert(result is Result.Success)
    }

    @Test
    fun `When length is more than 9, uppercase and digit, success`() = runTest {
        // Arrange
        val password = "long_enough_Uppercase_digit9_SpecialCharacters"
        // Act
        val result = userDataValidator.validatePassword(password)
        // Assert
        assert(result is Result.Success)
    }

    @Test
    fun `Email with wrong format return false, only correct format returns true`() = runTest {
        // Act and assert
        var validation = userDataValidator.validateEmail("")
        assert(!validation)
        validation = userDataValidator.validateEmail("ho")
        assert(!validation)
        validation = userDataValidator.validateEmail("ho@.com")
        assert(!validation)
        validation = userDataValidator.validateEmail("@gmail.com")
        assert(!validation)
        validation = userDataValidator.validateEmail("ho@gmailcom")
        assert(!validation)
        validation = userDataValidator.validateEmail("ho@gmail.")
        assert(!validation)
        validation = userDataValidator.validateEmail("h@o@gmailcom")
        assert(!validation)
        validation = userDataValidator.validateEmail("h\$o@gmail.")
        assert(!validation)
        validation = userDataValidator.validateEmail("something_-+.@something.something")
        assert(validation)
    }

    
    
}