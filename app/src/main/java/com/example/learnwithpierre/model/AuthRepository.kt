package com.example.learnwithpierre.model

import com.google.android.gms.auth.api.identity.SignInCredential
import kotlinx.coroutines.CoroutineScope

interface AuthRepository {

    fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse

    suspend fun signInAnonymously(): FirebaseSignInResponse

    suspend fun signOut(): SignOutResponse

    suspend fun onTapSignIn(): OneTapSignInResponse

    suspend fun signInWithGoogle(credential: SignInCredential): FirebaseSignInResponse
}