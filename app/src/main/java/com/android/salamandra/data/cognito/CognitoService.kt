package com.android.salamandra.data.cognito

import android.util.Log
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import com.android.salamandra.data.model.TokenManager
import javax.inject.Inject

class CognitoService @Inject constructor(
    private val tokenManager: TokenManager
) {
    fun login(email: String, password: String): Boolean {
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
                            tokenManager.saveToken(cognitoAuthSession.accessToken!!)
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
            .build()
        var registerWorked = false
        Amplify.Auth.signUp(
            email,
            password,
            options,
            {//onSuccess
                Log.i("SLM", "Amplify register worked")
                registerWorked = true
            },
            {//onError
                Log.e("SLM", "Sign up failed: ${it.message}")
                throw (it)
            }
        )
        return registerWorked
    }

    fun confirmRegister(username: String, code: String): Boolean {
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
                            tokenManager.saveToken(cognitoAuthSession.accessToken!!)
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

    fun isUserLoged(): Boolean {
        var isLogged = false
        Amplify.Auth.fetchAuthSession(
            { result ->
                val cognitoAuthSession: AWSCognitoAuthSession = result as AWSCognitoAuthSession
                isLogged = cognitoAuthSession.isSignedIn
                if(!isLogged) {
                    tokenManager.deleteToken()
                } else {
                    tokenManager.saveToken(cognitoAuthSession.accessToken!!)
                }
            },
            { fetchError ->
                Log.e("SLM", "Failed to fetch session", fetchError)
                throw fetchError
            }
        )
        return isLogged
    }
}