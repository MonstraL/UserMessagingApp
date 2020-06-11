package com.stepan.develop.usermessagingapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.google.firebase.auth.FirebaseUser
import com.stepan.develop.usermessagingapp.R
import com.stepan.develop.usermessagingapp.di.components.DaggerIMainActivityComponent
import com.stepan.develop.usermessagingapp.di.components.IMainActivityComponent
import com.stepan.develop.usermessagingapp.di.modules.MainActivityModule
import com.stepan.develop.usermessagingapp.presenter.IMainActivityPresenter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), IMainActivityView {

    @Inject
    lateinit var presenter: IMainActivityPresenter

    private lateinit var mainActivityComponent: IMainActivityComponent
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupDependencies()
        presenter.attach(this)
        signInWithPhoneButton.setOnClickListener { presenter.onPhoneAuthButtonClick() }
        sendSMSButton.setOnClickListener { presenter.onSendSMSButtonClick(phoneNumber.text.toString()) }
        phoneOutButton.setOnClickListener { presenter.onPhoneAuthOutButtonClick() }
        verifyButton.setOnClickListener { verifyCode.text?.let{presenter.verifyAuthCode(it.toString())} }
    }

    override fun setCallbackManager(callbackManager: CallbackManager) {
        this.callbackManager = callbackManager
    }

    override fun updateUserLoginFields(user: FirebaseUser?) {
        if(user != null) {
            if(user.email != null && user.email!!.isNotEmpty()) {
                userEmail.setText(user.email)
            } else {
                userEmail.setText(user.phoneNumber)
            }

        } else {
            userEmail.setText("")
        }
    }

    override fun showPhoneNumberFields() {
        signInWithPhoneButton.visibility = View.GONE
        phoneNumberLayout.visibility = View.VISIBLE
    }

    override fun hidePhoneNumberFields() {
        phoneNumberLayout.visibility = View.GONE
        signInWithPhoneButton.visibility = View.VISIBLE
    }

    override fun showVerifyFields() {
        verifyCodeLayout.visibility = View.VISIBLE
    }

    override fun hideVerifyFields() {
        verifyCodeLayout.visibility = View.INVISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupDependencies() {
        mainActivityComponent = DaggerIMainActivityComponent.builder()
            .mainActivityModule(MainActivityModule(this))
            .build()

        mainActivityComponent.inject(this)
    }

    override fun showToast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        presenter.addAuthStateListener()
    }

    override fun onStop() {
        super.onStop()
        presenter.removeAuthStateListener()
    }
}
