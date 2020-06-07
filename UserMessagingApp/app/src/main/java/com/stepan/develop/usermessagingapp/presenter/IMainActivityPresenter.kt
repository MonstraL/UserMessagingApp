package com.stepan.develop.usermessagingapp.presenter

import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.stepan.develop.usermessagingapp.view.IMainActivityView

interface IMainActivityPresenter {
    fun attach(mainActivity: IMainActivityView)
    fun facebookOnSuccess(result: LoginResult?)
    fun facebookOnError(error: FacebookException?)
    fun facebookOnCancel()

    fun addAuthStateListener()
    fun removeAuthStateListener()
}