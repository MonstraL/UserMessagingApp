package com.stepan.develop.usermessagingapp.di.modules

import android.app.Activity
import com.stepan.develop.usermessagingapp.presenter.IMainActivityPresenter
import com.stepan.develop.usermessagingapp.presenter.MainActivityPresenterImpl
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule(private val activity: Activity) {

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun providePresenter(): IMainActivityPresenter {
        return MainActivityPresenterImpl()
    }
}