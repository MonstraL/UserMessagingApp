package com.stepan.develop.usermessagingapp.presenter

import android.util.Log
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.stepan.develop.usermessagingapp.view.IMainActivityView
import com.stepan.develop.usermessagingapp.view.MainActivity
import java.util.concurrent.TimeUnit

class MainActivityPresenterImpl: IMainActivityPresenter{

    private val TAG = "MainActivityPresenterImpl"

    private lateinit var view: IMainActivityView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    private lateinit var mAccessTokenTracker: AccessTokenTracker
    private lateinit var mVerificationId: String
    private lateinit var mResendToken: PhoneAuthProvider.ForceResendingToken

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

    override fun onPhoneAuthButtonClick() {
        view.showPhoneNumberFields()
    }

    override fun onSendSMSButtonClick(phoneNumber: String) {
        PhoneAuthProvider.getInstance(mAuth).verifyPhoneNumber(
            phoneNumber,        // Phone number to verify
            60,                 // Timeout duration
            TimeUnit.SECONDS,   // Unit of timeout
            view as MainActivity,               // Activity (for callback binding)
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d(TAG, "onVerificationCompleted:" + credential);
                    signInWithAuthCredential(credential);
                }


                override fun onVerificationFailed(e: FirebaseException) {
                    Log.w(TAG, "onVerificationFailed", e);
                    println("Something went wrong")
                }


                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    Log.d(TAG, "onCodeSent:" + verificationId);
                    mVerificationId = verificationId
                    mResendToken = token;
                }
            }
        )

        view.showVerifyFields()
    }

    override fun onPhoneAuthOutButtonClick() {
        mAuth.signOut()
        LoginManager.getInstance().logOut()
        view.hidePhoneNumberFields()
        view.hideVerifyFields()
    }

    override fun verifyAuthCode(code: String) {
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, code)
        signInWithAuthCredential(credential)
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
        val authCred = FacebookAuthProvider.getCredential(accessToken.token)
        signInWithAuthCredential(authCred)
    }

    private fun signInWithAuthCredential(authCred: AuthCredential){
        mAuth.signInWithCredential(authCred)
            .addOnCompleteListener { if(it.isSuccessful) {
                val fbUser = mAuth.currentUser
                fbUser?.let {
                        user -> view.updateUserLoginFields(user)
                        view.hideVerifyFields()
                }
            } else {
                view.showToast( "Couldn't register to Firebase")
            }
            }
    }

    private fun updateCurrentUser() {
        view.updateUserLoginFields(mAuth.currentUser)
    }
}