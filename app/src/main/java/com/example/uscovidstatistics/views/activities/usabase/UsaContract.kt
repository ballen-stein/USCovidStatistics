package com.example.uscovidstatistics.views.activities.usabase

import android.content.Context
import com.example.uscovidstatistics.model.apidata.StateDataset
import com.example.uscovidstatistics.presenter.BasePresenter
import com.example.uscovidstatistics.presenter.BaseView

interface UsaContract {
    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun getStateData(regionList: Array<String>)
        fun onServiceStarted(context: Context)
    }

    interface View : BaseView<Presenter> {
        fun displayAllStates(stateDataset: StateDataset)
        fun dataError(onError: Throwable)
    }
}