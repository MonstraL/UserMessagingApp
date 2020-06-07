package com.stepan.develop.usermessagingapp.app

import android.app.Application
import com.stepan.develop.usermessagingapp.di.components.DaggerIUserMessagingAppComponent
import com.stepan.develop.usermessagingapp.di.components.IUserMessagingAppComponent

class UserMessagingApp: Application() {
    private lateinit var userMessagingAppComponent: IUserMessagingAppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        setupDependencies()
    }

    private fun setupDependencies() {
        userMessagingAppComponent = DaggerIUserMessagingAppComponent.create()
    }

    fun getApplicationComponent(): IUserMessagingAppComponent {
        return userMessagingAppComponent
    }

    companion object {
        lateinit var instance: UserMessagingApp
            private set
    }

}