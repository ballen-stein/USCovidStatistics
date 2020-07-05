package com.example.uscovidstatistics.views.activities.country

import android.content.Context
import com.example.uscovidstatistics.model.apidata.JhuCountryDataset
import com.example.uscovidstatistics.presenter.BasePresenter
import com.example.uscovidstatistics.presenter.BaseView

interface CountryContract {
    interface Presenter : BasePresenter {
        fun onViewCreated(countryName: String)
        fun getRegionalData(getSpecifics: Int, regionList: Array<String>)
        fun onServiceStarted(context: Context)
        fun networkStatus(mContext: Context)
    }

    interface View : BaseView<Presenter> {
        fun displayCountryData(countryData: JhuCountryDataset)
        fun displayCountryList()
        fun displayUsList()
        fun dataError(throwable: Throwable)
        fun onResumeData()
    }
}