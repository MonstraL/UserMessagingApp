package com.stepan.develop.usermessagingapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

    lateinit var mainActivityComponent: IMainActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupDependencies()
        presenter.attach(this)
    }

    private fun setupDependencies() {
        mainActivityComponent = DaggerIMainActivityComponent.builder()
            .mainActivityModule(MainActivityModule(this))
            .build()

        mainActivityComponent.inject(this)
    }

    override fun showSomeText(someText: String) {
        textField.setText(someText)
    }
}
