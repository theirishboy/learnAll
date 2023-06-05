package com.example.learnwithpierre

import android.app.Application
import com.example.learnwithpierre.dao.AppContainer
import com.example.learnwithpierre.dao.AppDataContainer

class LearnApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
