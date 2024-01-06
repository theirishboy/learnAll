package com.example.learnwithpierre

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.learnwithpierre.ui.theme.LearnWithPierreTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()

        )
        if(BuildConfig.DEBUG)
            StrictMode.enableDefaults()

        super.onCreate(savedInstanceState)
        setContent {
            LearnWithPierreTheme {

                LearnAllApp()
            }
        }
    }
}

