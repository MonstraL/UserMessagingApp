package com.stepan.develop.usermessagingapp.presenter

import com.stepan.develop.usermessagingapp.view.IMainActivityView

class MainActivityPresenterImpl: IMainActivityPresenter{
    private lateinit var view: IMainActivityView

    override fun attach(mainActivity: IMainActivityView) {
        this.view = mainActivity
    }

}