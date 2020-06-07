package com.stepan.develop.usermessagingapp.view

import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseUser

interface IMainActivityView {
    fun showToast(text: String)
    fun setCallbackManager(callbackManager: CallbackManager)
    fun updateUserLoginFields(user: FirebaseUser?)
}