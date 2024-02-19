package com.android.salamandra.data.cognito

import android.util.Log
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthSession
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.core.Amplify
import javax.inject.Inject

class CognitoService @Inject constructor(){
    fun login(email: String, password: String) {
        Amplify.Auth.signIn(
            email,
            password,
            { //on Success
                Log.i("Jaime", "Sign in worked")
                Amplify.Auth.fetchAuthSession(
                    { result ->
                        val cognitoAuthSession: AWSCognitoAuthSession = result as AWSCognitoAuthSession
                        val token = cognitoAuthSession.accessToken
                        val token2 = cognitoAuthSession.tokensResult.value?.accessToken
                        val refreshToken = cognitoAuthSession.tokensResult.value?.idToken
                        val idToken = cognitoAuthSession.tokensResult.value?.refreshToken
                        Log.i("Jaime", "token: $token")
                        Log.i("Jaime", "token2: $token2")
                        Log.i("Jaime", "idToken: $idToken")
                        Log.i("Jaime", "refreshToken: $refreshToken")

                    },
                    { error ->
                        Log.e("Jaime", "Failed to fetch session", error)
                    }
                )
            },
            { //on Failure
                Log.e("Jaime", "Sign in not complete ${it.message}")
                throw it
            }
        )


    }

    fun register(email: String, password: String, username: String) { //throws exception
        val options = AuthSignUpOptions.builder()
            .build()

        Amplify.Auth.signUp(
            email ,
            password,
            options,
            {//onSuccess
                Log.i("Jaime", "Amplify register worked")


            },
            {//onError
                Log.e("Jaime", "Sign up failed: ${it.message}")
                throw(it)
            }
        )
    }

    fun confirmRegister(username: String, code: String){
        try {
            Amplify.Auth.confirmSignUp(
                username,
                code,
                {//onSuccess
                    Log.i("Jaime", "Signup confirmed")
                },
                {//onError
                    Log.i("Jaime", "Signup confirmation not yet complete: ${it.message}")
                }
            )
        } catch (error: AuthException) {
            Log.e("AuthQuickstart", "Failed to confirm signup", error)
        }

    }
}