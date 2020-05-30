package com.stepan.develop.usermessagingapp.di.components

import com.stepan.develop.usermessagingapp.app.UserMessagingApp
import com.stepan.develop.usermessagingapp.di.modules.UserMessagingAppModule
import dagger.Component

@Component(modules = [UserMessagingAppModule::class])
interface IUserMessagingAppComponent {
    fun inject(application: UserMessagingApp)
}