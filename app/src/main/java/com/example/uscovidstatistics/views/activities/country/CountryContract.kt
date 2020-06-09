package com.example.uscovidstatistics.views.activities.country

import android.content.Context
import com.example.uscovidstatistics.model.apidata.JhuCountryDataset
import com.example.uscovidstatistics.presenter.BasePresenter
import com.example.uscovidstatistics.presenter.BaseView

interface CountryContract {
    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun getRegionalData(regionList: Array<String>)
        fun onDataUpdated()
        fun onServiceStarted(context: Context)
    }

    interface View : BaseView<Presenter> {
        fun displayCountryData(countryData: JhuCountryDataset)
        fun displayCountryList()
        fun dataError()
    }
}