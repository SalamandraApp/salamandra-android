package com.android.salamandra.data.cognito

import android.util.Log
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
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
                try {
                    val cognitoAuthSession =
                        Amplify.Auth.fetchAuthSession() as AWSCognitoAuthSession
                    dataStore.saveToken(cognitoAuthSession.accessToken!!)
                    Result.Success(Unit)

                } catch (e: Exception) {
                    Log.e("SLM", "Error using Cognito for sign in: " + e.message.orEmpty())
                    Result.Error(DataError.Cognito.SESSION_FETCH)
                }
            } else {
                Result.Error(DataError.Cognito.INVALID_EMAIL_OR_PASSWORD)
            }
        } catch (e: Exception) {
            Log.e("SLM", "Error using Cognito for sign in: " + e.message.orEmpty())
            return Result.Error(DataError.Cognito.INVALID_EMAIL_OR_PASSWORD)
        }
    }


    suspend fun register(
        email: String,
        password: String,
        username: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) { //throws exception
//        val options = AuthSignUpOptions.builder()
//            .userAttribute(AuthUserAttributeKey.email(), email)
//            .build()
//        Amplify.Auth.signUp(
//            username,
//            password,
//            options,
//            {//onSuccess
//                Log.i("SLM", "Amplify register worked")
//                onSuccess()
//            },
//            {//onError
//                Log.e("SLM", "Sign up failed: ${it.message}")
//                onError(it)
//
//            }
//        )
    }

    suspend fun confirmRegister(
        username: String,
        code: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
//        Amplify.Auth.confirmSignUp(
//            username,
//            code,
//            {//onSuccess
//                Log.i("SLM", "Signup confirmed")
////                Amplify.Auth.fetchAuthSession(
////                    { result ->
////                        val cognitoAuthSession: AWSCognitoAuthSession =
////                            result as AWSCognitoAuthSession
////                        runBlocking {
////                            dataStore.saveToken(cognitoAuthSession.accessToken!!)
////                        }
////                        onSuccess()
////                    },
////                    { fetchError ->
////                        Log.e("SLM", "Failed to fetch session", fetchError)
////                        onError(fetchError)
////                    }
////                )
//                onSuccess()
//            },
//            { confirmationError: AuthException ->//onError
//                Log.e("SLM", "Signup confirmation not yet complete: ${confirmationError.message}")
//                onError(confirmationError)
//            }
//        )
    }

    suspend fun isUserLoged(): Boolean {
//        var isLogged = false
//        Amplify.Auth.fetchAuthSession(
//            { result ->
//                val cognitoAuthSession: AWSCognitoAuthSession = result as AWSCognitoAuthSession
//                isLogged = cognitoAuthSession.isSignedIn
//                if (!isLogged) {
//                    runBlocking {
//                        dataStore.deleteToken()
//                    }
//                } else {
//                    runBlocking {
//                        dataStore.saveToken(cognitoAuthSession.accessToken!!)
//                    }
//                }
//            },
//            { fetchError ->
//                Log.e("SLM", "Failed to fetch session", fetchError)
//                throw fetchError
//            }
//        )
//        return isLogged
        TODO()
    }

    suspend fun logout() {
        when (Amplify.Auth.signOut()) {
            is AWSCognitoAuthSignOutResult.CompleteSignOut -> {
                Log.i("SLM", "Sign out complete")
            }

            is AWSCognitoAuthSignOutResult.PartialSignOut -> {
                Log.e("SLM", "Partial sign out, some data may still be on the device")
            }

            is AWSCognitoAuthSignOutResult.FailedSignOut -> {
                Log.e("SLM", "Sign out failed")
            }
        }
    }
}