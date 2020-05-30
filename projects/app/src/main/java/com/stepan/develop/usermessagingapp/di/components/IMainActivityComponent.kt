package com.stepan.develop.usermessagingapp.di.components

import com.stepan.develop.usermessagingapp.di.modules.MainActivityModule
import com.stepan.develop.usermessagingapp.view.MainActivity
import dagger.Component

@Component(modules = [MainActivityModule::class])
interface IMainActivityComponent {
    fun inject(activity: MainActivity)
}