package com.stepan.develop.usermessagingapp.di.modules

import android.app.Application
import com.stepan.develop.usermessagingapp.app.UserMessagingApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserMessagingAppModule(private val app: UserMessagingApp) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return app
    }
}