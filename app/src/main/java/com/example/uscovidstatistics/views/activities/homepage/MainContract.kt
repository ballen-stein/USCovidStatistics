package com.example.uscovidstatistics.views.activities.homepage

import com.example.uscovidstatistics.presenter.BasePresenter
import com.example.uscovidstatistics.presenter.BaseView

interface MainContract {
    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun onDataUpdated()
    }

    interface View : BaseView<Presenter> {
        fun displayContinentData(continentData: IntArray)
    }
}