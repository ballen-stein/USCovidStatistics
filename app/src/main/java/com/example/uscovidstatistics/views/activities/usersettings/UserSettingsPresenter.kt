package com.example.uscovidstatistics.views.activities.usersettings

import android.content.Context
import com.example.uscovidstatistics.manualdependency.DependencyInjectorImpl
import com.example.uscovidstatistics.model.DataModelRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class UserSettingsPresenter(view: UserSettingsContract.View) : UserSettingsContract.Presenter {

    private var view: UserSettingsContract.View? = view

    override fun onDestroy() {
        this.view = null
    }

    override fun onViewCreated() {
        loadSettings()
    }

    override fun loadSettings() {
        TODO("Not yet implemented")
    }

    override fun onServiceStarted(context: Context) {
        TODO("Not yet implemented")
    }
}