package com.example.learnwithpierre.model

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class AuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth,
                                             private var oneTapClient: SignInClient,
                                             private val dataProvider: DataProvider,
                                             @Named("signInRequest") private var signInRequest: BeginSignInRequest,
                                             @Named("signUpRequest") private var signUpRequest: BeginSignInRequest,
): AuthRepository {
    override fun getAuthState(viewModelScope: CoroutineScope) = callbackFlow {
        val authStateListener = AuthStateListener { auth ->
            trySend(auth.currentUser)
            Log.i(TAG, "User: ${auth.currentUser?.uid ?: "Not authenticated"}")
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), auth.currentUser)

    override suspend fun signInAnonymously(): FirebaseSignInResponse {
        return try {
            val authResult = auth.signInAnonymously().await()
            authResult?.user?.let { user ->
                Log.i(TAG, "FirebaseAuthSuccess: Anonymous UID: ${user.uid}")
            }
            Response.Success(authResult)
        } catch (error: Exception) {
            Log.e(TAG, "FirebaseAuthError: Failed to Sign in anonymously")
            Response.Failure(error)
        }
    }

    override suspend fun signOut(): SignOutResponse {
        return try {
            auth.signOut()
            dataProvider.updateAuthState(null)
            Response.Success(true)
        }
        catch (e: java.lang.Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun onTapSignIn(): OneTapSignInResponse {
        return try {
            // 1.
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            Response.Success(signInResult)
        } catch (e: Exception) {
            try {
                // 2.
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                Response.Success(signUpResult)
            } catch(e: Exception) {
                Response.Failure(e)
            }
        }
    }
    override suspend fun signInWithGoogle(credential: SignInCredential): FirebaseSignInResponse {
        // 1.
        val googleCredential = GoogleAuthProvider
            .getCredential(credential.googleIdToken, null)
        Log.d("GOOGLE Sign", "google credential $googleCredential")
        // 2.
        return authenticateUser(googleCredential)
    }

    // 1.
    private suspend fun authenticateUser(credential: AuthCredential): FirebaseSignInResponse {
        // If we have auth user, link accounts, otherwise sign in.
        return if (auth.currentUser != null) {
            authLink(credential)
        } else {
            authSignIn(credential)
        }
    }

    private suspend fun authSignIn(credential: AuthCredential): FirebaseSignInResponse {
        return try {
            val authResult = auth.signInWithCredential(credential).await()
            Log.i(TAG, "User: ${authResult?.user?.uid}")
            dataProvider.updateAuthState(authResult?.user)
            Response.Success(authResult)
        }
        catch (error: Exception) {
            Response.Failure(error)
        }
    }

    // 3.
    private suspend fun authLink(credential: AuthCredential): FirebaseSignInResponse {
        return try {
            val authResult = auth.currentUser?.linkWithCredential(credential)?.await()
            Log.i(TAG, "User: ${authResult?.user?.uid}")
            dataProvider.updateAuthState(authResult?.user)
            Response.Success(authResult)
        }
        catch (error: Exception) {
            Response.Failure(error)
        }
        catch (error: FirebaseException) {
            error.message?.let {
                if (it.contains("User has already been linked to the given provider" )) {
                    return authSignIn(credential)
                }
            }
            Log.e(TAG, "FirebaseError: authLink(credential:) failed, ${error.message}")
            Response.Failure(error)
        }
        catch (error: FirebaseAuthException) {
            when (error.errorCode) {
                "ERROR_CREDENTIAL_ALREADY_IN_USE",
                "ERROR_EMAIL_ALREADY_IN_USE" -> {
                    Log.e(TAG, "FirebaseAuthError: authLink(credential:) failed, ${error.message}")
                    return authSignIn(credential)
                }
            }
            Response.Failure(error)
        }
    }


}