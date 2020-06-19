package com.example.uscovidstatistics.views.activities.region

import android.content.Context
import com.example.uscovidstatistics.model.apidata.StateDataset
import com.example.uscovidstatistics.presenter.BasePresenter
import com.example.uscovidstatistics.presenter.BaseView

interface StateContract {
    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun getStateData(): List<StateDataset>
    }

    interface View : BaseView<Presenter> {
        fun displayStateData(stateDataset: StateDataset)
        fun dataError(throwable: Throwable)
    }
}