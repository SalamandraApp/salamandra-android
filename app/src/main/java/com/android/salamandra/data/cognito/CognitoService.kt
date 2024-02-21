package com.android.salamandra.data.cognito

import android.util.Log
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import com.android.salamandra.domain.DataStoreRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class CognitoService @Inject constructor(
    private val dataStore: DataStoreRepository
) {
    suspend fun login(email: String, password: String): Boolean {
        var loginResult = false
        Amplify.Auth.signIn(
            email,
            password,
            { success -> //on Success
                Log.i("SLM", "Sign in worked")
                if (success.isSignedIn) {
                    Amplify.Auth.fetchAuthSession(
                        { result ->
                            val cognitoAuthSession: AWSCognitoAuthSession =
                                result as AWSCognitoAuthSession
                            runBlocking {
                                dataStore.saveToken(cognitoAuthSession.accessToken!!)
                            }
                            loginResult = true
                        },
                        { fetchError ->
                            Log.e("Jaime", "Failed to fetch session", fetchError)
                            throw fetchError
                        }
                    )
                }
            },
            {signInError -> //on Failure
                Log.e("SLM", "Sign in not complete ${signInError.message}")
                throw signInError
            }
        )
        return loginResult
    }

    fun register(email: String, password: String, username: String): Boolean { //throws exception
        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .userAttribute(AuthUserAttributeKey.nickname(), username)
            .build()
        var registerWorked = false
        Amplify.Auth.signUp(
            username,
            password,
            options,
            {//onSuccess
                Log.i("SLM", "Amplify register worked")
                registerWorked = true
            },
            {//onError
                Log.e("SLM", "Sign up failed: ${it.message}")
                throw (Exception(it.message))
            }
        )
        return registerWorked
    }

    suspend fun confirmRegister(username: String, code: String): Boolean {
        var confirmResult = false
        try {
            Amplify.Auth.confirmSignUp(
                username,
                code,
                {//onSuccess
                    Log.i("SLM", "Signup confirmed")
                    Amplify.Auth.fetchAuthSession(
                        { result ->
                            val cognitoAuthSession: AWSCognitoAuthSession =
                                result as AWSCognitoAuthSession
                            runBlocking {
                                dataStore.saveToken(cognitoAuthSession.accessToken!!)
                            }
                            confirmResult = true
                        },
                        { fetchError ->
                            Log.e("SLM", "Failed to fetch session", fetchError)
                            throw fetchError
                        }
                    )
                },
                {//onError
                    Log.e("SLM", "Signup confirmation not yet complete: ${it.message}")
                }
            )
        } catch (error: AuthException) {
            Log.e("SLM", "Failed to confirm signup", error)
        }
        return confirmResult
    }

    suspend fun isUserLoged(): Boolean {
        var isLogged = false
        Amplify.Auth.fetchAuthSession(
            { result ->
                val cognitoAuthSession: AWSCognitoAuthSession = result as AWSCognitoAuthSession
                isLogged = cognitoAuthSession.isSignedIn
                if(!isLogged) {
                    runBlocking {
                        dataStore.deleteToken()
                    }
                } else {
                    runBlocking {
                        dataStore.saveToken(cognitoAuthSession.accessToken!!)
                    }
                }
            },
            { fetchError ->
                Log.e("SLM", "Failed to fetch session", fetchError)
                throw fetchError
            }
        )
        return isLogged
    }

    fun logout() {
        Amplify.Auth.signOut { signOutResult ->
            when(signOutResult) {
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
}