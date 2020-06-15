package com.example.uscovidstatistics.views.activities.region

import android.content.Context
import android.util.Log
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.manualdependency.DependencyInjectorImpl
import com.example.uscovidstatistics.model.DataModelRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class RegionPresenter(view: RegionContract.View, dependencyInjectorImpl: DependencyInjectorImpl) : RegionContract.Presenter{

    private val dataModelRepository: DataModelRepository = dependencyInjectorImpl.covidDataRepository()

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private var view: RegionContract.View? = view

    override fun onDestroy() {
        this.view = null
    }

    override fun onViewCreated() {
        Log.d("CovidTesting", "${AppConstants.DATA_SPECIFICS}, ${AppConstants.REGION_NAME}, ${AppConstants.COUNTRY_NAME}")
    }

    override fun getRegionalData(regionList: Array<String>) {
        TODO("Not yet implemented")
    }

    override fun onServiceStarted(context: Context) {
        TODO("Not yet implemented")
    }

}