package com.example.learnwithpierre.ui.screen

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.learnwithpierre.R
import com.example.learnwithpierre.model.AuthState
import com.example.learnwithpierre.model.DataProvider
import com.example.learnwithpierre.model.Response
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    loginState: MutableState<Boolean>? = null
) {
    val dataProvider = authViewModel.dataProvider
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                // 2.
                val credentials = authViewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                authViewModel.signInWithGoogle(credentials)
            }
            catch (e: ApiException) {
                Log.e("LoginScreen:Launcher","Login One-tap $e")
            }
        }
        else if (result.resultCode == Activity.RESULT_CANCELED){
            Log.e("LoginScreen:Launcher","OneTapClient Canceled")
        }
    }

    fun launch(signInResult: BeginSignInResult) {
        // 3.
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }
    // Scaffold content...
    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .wrapContentSize(Alignment.TopCenter),
            Arrangement.spacedBy(8.dp),
            Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(1f),
                painter = painterResource(R.drawable.loginscreen),
                contentDescription = "app_logo",
                contentScale = ContentScale.Fit,
              //  colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.tertiary)
            )

            Button(
                onClick = {
                    authViewModel.oneTapSignIn()
                },
                modifier = Modifier
                    .size(width = 300.dp, height = 50.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = ""
                )
                Text(
                    text = "Sign in with Google",
                    modifier = Modifier.padding(6.dp),
                    color = Color.Black.copy(alpha = 0.5f)
                )
            }

            if (dataProvider.authState == AuthState.SignedOut) {
                Button(
                    onClick = {
                        authViewModel.signInAnonymously()
                    },
                    modifier = Modifier
                        .size(width = 200.dp, height = 50.dp)
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = "Skip",
                        modifier = Modifier.padding(6.dp),
               //         color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }

    when (val anonymousResponse = dataProvider.anonymousSignInResponse) {
        is Response.Loading -> {
            Log.i("Login:AnonymousSignIn", "Loading")
            AuthLoginProgressIndicator()
        }
        is Response.Success -> anonymousResponse.data?.let {
            authResult -> Log.i("Login:AnonymousSignIn", "Success: $authResult")
        }
        is Response.Failure -> {
            Log.e("Login:AnonymousSignIn", "${anonymousResponse.e}")
        }
    }
    when(val oneTapSignInResponse = dataProvider.oneTapSignInResponse) {
        // 1.
        is Response.Loading ->  {
            Log.i("Login:OneTap", "Loading")
            AuthLoginProgressIndicator()
        }
        // 2.
        is Response.Success -> oneTapSignInResponse.data?.let {
            signInResult -> LaunchedEffect(signInResult) {
                launch(signInResult)
            }
        }
        is Response.Failure -> LaunchedEffect(Unit) {
            Log.e("Login:OneTap", "${oneTapSignInResponse.e}")
        }
    }

    when (val signInWithGoogleResponse = dataProvider.googleSignInResponse) {
        // 1.
        is Response.Loading -> {
            Log.i("Login:GoogleSignIn", "Loading")
            AuthLoginProgressIndicator()
        }
        // 3.
        is Response.Success -> signInWithGoogleResponse.data?.let { authResult ->
            Log.i("Login:GoogleSignIn", "Success: $authResult")
            loginState?.let { it.value = false }
        }
        is Response.Failure -> {
            Log.e("Login:GoogleSignIn", "${signInWithGoogleResponse.e}")
        }
    }
}
@Composable
fun AuthLoginProgressIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.tertiary,
            strokeWidth = 5.dp
        )
    }
}