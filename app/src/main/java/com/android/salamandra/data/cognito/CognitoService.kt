package com.android.salamandra.data.cognito

import android.util.Log
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthSignOutResult
import com.amplifyframework.auth.result.step.AuthSignUpStep
import com.amplifyframework.kotlin.core.Amplify
import com.android.salamandra.domain.DataStoreRepository
import com.android.salamandra.domain.error.DataError
import com.android.salamandra.domain.error.Result
import javax.inject.Inject

class CognitoService @Inject constructor(
    private val dataStore: DataStoreRepository
) {
    suspend fun login(
        email: String,
        password: String
    ): Result<Unit, DataError.Cognito> {
        try {
            return if (Amplify.Auth.signIn(email, password).isSignedIn) {
                fetchAndSaveToken()
            } else {
                Result.Error(DataError.Cognito.INVALID_EMAIL_OR_PASSWORD)
            }
        } catch (e: Exception) {
            Log.e("SLM", "Error using Cognito for sign in: " + e.message.orEmpty())
            return Result.Error(DataError.Cognito.INVALID_EMAIL_OR_PASSWORD)
        }
    }

    private suspend fun fetchAndSaveToken(): Result<Unit, DataError.Cognito> = try {
        val cognitoAuthSession =
            Amplify.Auth.fetchAuthSession() as AWSCognitoAuthSession
        dataStore.saveToken(cognitoAuthSession.accessToken!!)
        Result.Success(Unit)

    } catch (e: Exception) {
        Log.e("SLM", "Error using Cognito for sign in: " + e.message.orEmpty())
        Result.Error(DataError.Cognito.SESSION_FETCH)
    }


    suspend fun register(
        email: String,
        password: String,
        username: String
    ): Result<Unit, DataError.Cognito> { //throws exception

        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build()
        return if (Amplify.Auth.signUp(
                username = username,
                password = password,
                options = options
            ).nextStep.signUpStep == AuthSignUpStep.CONFIRM_SIGN_UP_STEP
        ) Result.Success(Unit) else Result.Error(DataError.Cognito.SIGN_UP_FIELDS_NOT_VALID)

    }

    suspend fun confirmRegister(
        username: String,
        code: String
    ): Result<Unit, DataError.Cognito> {
        return if (Amplify.Auth.confirmSignUp(
                username = username,
                confirmationCode = code
            ).nextStep.signUpStep == AuthSignUpStep.DONE
        ) {
            fetchAndSaveToken()
        } else Result.Error(DataError.Cognito.WRONG_CONFIRMATION_CODE)
    }

    suspend fun isUserLogged(): Boolean {
        val cognitoAuthSession =
            Amplify.Auth.fetchAuthSession() as AWSCognitoAuthSession
        return cognitoAuthSession.isSignedIn
    }

    suspend fun logout(): Result<Unit, DataError.Cognito> {
        return when(val signOut = Amplify.Auth.signOut()) {
            is AWSCognitoAuthSignOutResult.CompleteSignOut -> {
                Log.i("SLM", "Signed out successfully")
                Result.Success(Unit)
            }
            is AWSCognitoAuthSignOutResult.PartialSignOut -> {
                signOut.hostedUIError?.let {
                    Log.e("AuthQuickStart", "HostedUI Error", it.exception)
                }
                signOut.globalSignOutError?.let {
                    Log.e("AuthQuickStart", "GlobalSignOut Error", it.exception)
                }
                signOut.revokeTokenError?.let {
                    Log.e("AuthQuickStart", "RevokeToken Error", it.exception)
                }
                Result.Error(DataError.Cognito.SIGN_OUT_FAILED_USER_NOT_SIGNED_IN)
            }
            is AWSCognitoAuthSignOutResult.FailedSignOut -> {
                Log.e("SLM", "Sign out Failed", signOut.exception)
                Result.Error(DataError.Cognito.SIGN_OUT_FAILED_USER_SIGNED_IN)
            }

            else -> Result.Error(DataError.Cognito.UNKNOWN_ERROR)
        }
    }
}