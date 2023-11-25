package com.bleh.monify.feature_auth

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.bleh.monify.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleAuthClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth

    suspend fun register(email: String, password: String): RegisterResult {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()
        return try {
            val authResult = auth.createUserWithEmailAndPassword(trimmedEmail, trimmedPassword).await()
            val user = authResult.user
            Log.d(TAG, "createUserWithEmail:success")
            RegisterResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            Log.w(TAG, "createUserWithEmail:failure", e)
            RegisterResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun login(email: String, password: String): LoginResult {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()
        return try {
            val authResult = auth.signInWithEmailAndPassword(trimmedEmail, trimmedPassword).await()
            val user = authResult.user
            Log.d(TAG, "signInWithEmail:success")
            LoginResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            // Handle the exception and return null
            Log.w(TAG, "signInWithEmail:failure", e)
            LoginResult(
                data = null,
                errorMessage = e.message)
        }
    }

    suspend fun oneTapLogin(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildLoginRequest()
            ).await()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun loginWithIntent(intent: Intent): LoginResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            LoginResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            LoginResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun logout() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    fun getLoggedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    private fun buildLoginRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}