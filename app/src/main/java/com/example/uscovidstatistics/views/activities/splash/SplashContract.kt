package com.example.uscovidstatistics.views.activities.splash

import android.content.Context
import com.example.uscovidstatistics.presenter.BasePresenter
import com.example.uscovidstatistics.presenter.BaseView

interface SplashContract {
    interface Presenter : BasePresenter {
        fun onViewCreated(mContext: Context)
        fun getCountryFlags()
        fun requestGps()
    }
    interface View : BaseView<Presenter> {
        fun launchApp()
        fun onError(throwable: Throwable)
    }
}