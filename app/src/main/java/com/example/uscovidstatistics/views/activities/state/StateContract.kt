package com.example.uscovidstatistics.views.activities.state

import android.content.Context
import com.example.uscovidstatistics.model.apidata.StateDataset
import com.example.uscovidstatistics.presenter.BasePresenter
import com.example.uscovidstatistics.presenter.BaseView

interface StateContract {
    interface Presenter : BasePresenter {
        fun onViewCreated(mContext: Context)
        fun getStateData() : List<StateDataset>
        fun networkStatus(mContext: Context)
    }

    interface View : BaseView<Presenter> {
        fun displayStateData(stateDataset: StateDataset)
        fun dataError(throwable: Throwable)
        fun onResumeData()
    }
}