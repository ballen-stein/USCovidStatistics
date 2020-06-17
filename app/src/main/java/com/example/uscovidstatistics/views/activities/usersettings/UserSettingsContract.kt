package com.example.uscovidstatistics.views.activities.usersettings

import android.content.Context
import com.example.uscovidstatistics.model.apidata.StateDataset
import com.example.uscovidstatistics.presenter.BasePresenter
import com.example.uscovidstatistics.presenter.BaseView

interface UserSettingsContract {
    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun loadSettings()
        fun onServiceStarted(context: Context)
    }

    interface View : BaseView<Presenter> {
        fun displayAllStates(stateDataset: StateDataset)
        fun dataError(onError: Throwable)
    }
}