package com.example.learnwithpierre.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseUser

enum class AuthState {
    Authenticated, // Anonymously authenticated in Firebase.
    SignedIn, // Authenticated in Firebase using one of service providers, and not anonymous.
    SignedOut; // Not authenticated in Firebase.
}

object DataProvider {

    // 1.
    var anonymousSignInResponse by mutableStateOf<FirebaseSignInResponse>(Response.Success(null))
    var googleSignInResponse by mutableStateOf<FirebaseSignInResponse>(Response.Success(null))
    var signOutResponse by mutableStateOf<SignOutResponse>(Response.Success(false))

    var user by mutableStateOf<FirebaseUser?>(null)

    var isAuthenticated by mutableStateOf(false)

    var isAnonymous by mutableStateOf(false)

    var authState by mutableStateOf(AuthState.SignedOut)
        private set

    var oneTapSignInResponse by mutableStateOf<OneTapSignInResponse>(Response.Success(null))

    fun updateAuthState(user: FirebaseUser?) {
        this.user = user
        isAuthenticated = user != null
        isAnonymous = user?.isAnonymous ?: false

        authState = if (isAuthenticated) {
            if (isAnonymous) AuthState.Authenticated else AuthState.SignedIn
        } else {
            AuthState.SignedOut
        }
    }
}