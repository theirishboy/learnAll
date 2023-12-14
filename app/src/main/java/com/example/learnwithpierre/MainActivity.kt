package com.example.learnwithpierre

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.example.learnwithpierre.model.AuthState
import com.example.learnwithpierre.model.DataProvider
import com.example.learnwithpierre.ui.screen.AuthViewModel
import com.example.learnwithpierre.ui.screen.HomeScreen
import com.example.learnwithpierre.ui.screen.LoginScreen
import com.example.learnwithpierre.ui.screen.ProfileViewScreen
import com.example.learnwithpierre.ui.theme.LearnWithPierreTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .build()

        )
        if(BuildConfig.DEBUG)
            StrictMode.enableDefaults()

        super.onCreate(savedInstanceState)
        setContent {
            LearnWithPierreTheme {

                val currentUser = authViewModel.currentUser.collectAsState().value
                // 3.
                DataProvider.updateAuthState(currentUser)
                // 4.
                if (DataProvider.authState != AuthState.SignedOut) {

                    ProfileViewScreen()
                } else {
                    LoginScreen(authViewModel)
                }
            }
        }
    }
}

