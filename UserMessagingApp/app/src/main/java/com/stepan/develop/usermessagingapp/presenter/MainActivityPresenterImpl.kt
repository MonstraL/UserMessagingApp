package com.stepan.develop.usermessagingapp.presenter

import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.stepan.develop.usermessagingapp.view.IMainActivityView

class MainActivityPresenterImpl: IMainActivityPresenter{
    private lateinit var view: IMainActivityView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    private lateinit var mAccessTokenTracker: AccessTokenTracker
    private val callbackManager = CallbackManager.Factory.create()

    init {
        prepareFBLogin()
    }

    override fun attach(mainActivity: IMainActivityView) {
        this.view = mainActivity
        mainActivity.setCallbackManager(callbackManager)
        updateCurrentUser()
    }

    override fun facebookOnSuccess(result: LoginResult?) {
        view.showToast("facebookOnSuccess")
    }

    override fun facebookOnError(error: FacebookException?) {
        view.showToast("facebookOnError ${error?.localizedMessage}")
    }

    override fun facebookOnCancel() {
        view.showToast("facebookOnCancel")
    }

    override fun addAuthStateListener() {
        mAuth.addAuthStateListener(mAuthStateListener)
    }

    override fun removeAuthStateListener() {
        mAuth.removeAuthStateListener(mAuthStateListener)
    }


    private fun prepareFBLogin() {
        mAuth = FirebaseAuth.getInstance()

        LoginManager.getInstance().registerCallback(callbackManager, object:
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                result?.accessToken?.let { handleUserAccessToken(it) }
            }

            override fun onCancel() {
                view.showToast("Cancelled")
            }

            override fun onError(error: FacebookException?) {
                error?.localizedMessage?.let { view.showToast(it) }
            }
        })

        mAuthStateListener = FirebaseAuth.AuthStateListener {
            val user = mAuth.currentUser
            view.updateUserLoginFields(user)
        }

        mAccessTokenTracker = object : AccessTokenTracker(){
            override fun onCurrentAccessTokenChanged(
                oldAccessToken: AccessToken?,
                currentAccessToken: AccessToken?
            ) {
                if(currentAccessToken == null) {
                    mAuth.signOut()
                }
            }

        }
    }

    private fun handleUserAccessToken(accessToken: AccessToken) {
        var authCred = FacebookAuthProvider.getCredential(accessToken.token)
        mAuth.signInWithCredential(authCred)
            .addOnCompleteListener { if(it.isSuccessful) {
                    val fbUser = mAuth.currentUser
                    fbUser?.let { user -> view.updateUserLoginFields(user) }
                } else {
                    view.showToast( "Couldn't register to Firebase")
                }
            }
    }

    private fun updateCurrentUser() {
        view.updateUserLoginFields(mAuth.currentUser)
    }
}