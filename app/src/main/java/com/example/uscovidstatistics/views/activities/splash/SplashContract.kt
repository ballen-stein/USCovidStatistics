package com.example.uscovidstatistics.views.activities.splash

import com.example.uscovidstatistics.presenter.BasePresenter
import com.example.uscovidstatistics.presenter.BaseView

interface SplashContract {
    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun getPreliminaryData()
        fun getCountryFlags()
        fun requestGps()
    }
    interface View : BaseView<Presenter> {
        fun setPreliminaryData()
        fun setCountryFlags()
        fun onError(throwable: Throwable)
    }
}