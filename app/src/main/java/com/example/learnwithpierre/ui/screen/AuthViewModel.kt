package com.example.learnwithpierre.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnwithpierre.model.AuthRepository
import com.example.learnwithpierre.model.DataProvider
import com.example.learnwithpierre.model.Response
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    val dataProvider: DataProvider,
    val oneTapClient: SignInClient
): ViewModel() {


    var currentUser = getAuthState()

    init {
        // 2.
        CoroutineScope(Dispatchers.IO).launch {
            getAuthState()
        }
    }

    // 1.
    private fun getAuthState() = repository.getAuthState(viewModelScope)

    fun signInAnonymously() = CoroutineScope(Dispatchers.IO).launch {
        dataProvider.anonymousSignInResponse = Response.Loading
        dataProvider.anonymousSignInResponse = repository.signInAnonymously()
    }
    fun signOut() = CoroutineScope(Dispatchers.IO).launch {
        dataProvider.signOutResponse = Response.Loading
        dataProvider.signOutResponse = repository.signOut()
    }
    fun oneTapSignIn() = CoroutineScope(Dispatchers.IO).launch {
        dataProvider.oneTapSignInResponse = Response.Loading
        dataProvider.oneTapSignInResponse = repository.onTapSignIn()
    }

    fun signInWithGoogle(credentials: SignInCredential) = CoroutineScope(Dispatchers.IO).launch {
        dataProvider.googleSignInResponse = Response.Loading
        dataProvider.googleSignInResponse = repository.signInWithGoogle(credentials)
    }
}