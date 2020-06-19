package com.example.uscovidstatistics.views.activities.region

import android.content.Context
import android.util.Log
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.manualdependency.DependencyInjectorImpl
import com.example.uscovidstatistics.model.DataModelRepository
import com.example.uscovidstatistics.model.apidata.StateDataset
import com.example.uscovidstatistics.network.NetworkRequests
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Response
import java.lang.Exception

class StatePresenter(view: StateContract.View, dependencyInjectorImpl: DependencyInjectorImpl) : StateContract.Presenter{

    private val dataModelRepository: DataModelRepository = dependencyInjectorImpl.covidDataRepository()

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private var view: StateContract.View? = view

    override fun onDestroy() {
        this.view = null
    }

    override fun onViewCreated() {
        Log.d("CovidTesting", "${AppConstants.DATA_SPECIFICS}, ${AppConstants.REGION_NAME}, ${AppConstants.COUNTRY_NAME}")
        loadData()
    }

    private fun loadData() {
        Observable.defer {
            try {
                val networkRequests = NetworkRequests(AppConstants.DATA_SPECIFICS, AppConstants.REGION_NAME, AppConstants.COUNTRY_NAME).getLocationData()
                Observable.just(networkRequests)
            } catch (e: Exception) {
                Observable.error<Exception>(e)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { onNext -> onNext as Response
                    setData(onNext, AppConstants.DATA_SPECIFICS)
                },
            { onError -> view?.dataError(onError)},
            { view?.displayStateData(dataModelRepository.getUsState()) }
            )
    }

    private fun setData(response: Response, dataSpecifics: Int) {
        val body = response.body!!
        try {
            val jsonAdapter = moshi.adapter(StateDataset::class.java)
            AppConstants.US_STATE_DATA = jsonAdapter.fromJson(body.string())!!
        } catch (e: Exception) {
            view?.dataError(e)
        }
        body.close()
    }

    override fun getStateData(): List<StateDataset> {
        return dataModelRepository.getUsData()
    }
}