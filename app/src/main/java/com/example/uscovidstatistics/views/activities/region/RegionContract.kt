package com.example.uscovidstatistics.views.activities.region

import android.content.Context
import com.example.uscovidstatistics.model.apidata.JhuProvinceDataset
import com.example.uscovidstatistics.presenter.BasePresenter
import com.example.uscovidstatistics.presenter.BaseView

interface RegionContract {
    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun getRegionalData(regionList: Array<String>)
        fun onServiceStarted(context: Context)
    }

    interface View : BaseView<Presenter> {
        fun displayRegionData(regionDataset: JhuProvinceDataset)
        fun dataError()
    }
}