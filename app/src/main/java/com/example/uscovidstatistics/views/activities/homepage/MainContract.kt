package com.example.uscovidstatistics.views.activities.homepage

import android.content.Context
import com.example.uscovidstatistics.model.apidata.BaseCountryDataset
import com.example.uscovidstatistics.presenter.BasePresenter
import com.example.uscovidstatistics.presenter.BaseView
import com.example.uscovidstatistics.utils.AppUtils
import dagger.Component

@Component
interface MainContract {
    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun updateData()
        fun openLocationOnLaunch(mContext: Context)
        fun onServiceStarted()
        fun networkStatus(mContext: Context)
    }

    interface View : BaseView<Presenter> {
        fun displayContinentData(continentData: BaseCountryDataset)
        fun dataError(throwable: Throwable)
        fun onResumeData()
    }
}