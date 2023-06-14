package com.example.learnwithpierre

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.learnwithpierre.ui.theme.LearnWithPierreTheme


class MainActivity : ComponentActivity() {
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

                LearnAllApp()
            }
        }
    }
}

