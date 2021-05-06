package com.example.myentertainment

import android.app.Application
import com.example.myentertainment.dagger.components.BaseApplicationComponent
import com.example.myentertainment.dagger.components.DaggerBaseApplicationComponent

class BaseApplication : Application() {

    companion object {
        lateinit var baseApplicationComponent: BaseApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        /*baseApplicationComponent = DaggerBaseApplicationComponent.builder()
            .firebaseModule(FirebaseModule())
            .build()*/

        baseApplicationComponent = DaggerBaseApplicationComponent.create()
    }
}