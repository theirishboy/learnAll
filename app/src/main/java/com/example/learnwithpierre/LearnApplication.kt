package com.example.learnwithpierre

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LearnApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppCardContainer(this)
    }
}
