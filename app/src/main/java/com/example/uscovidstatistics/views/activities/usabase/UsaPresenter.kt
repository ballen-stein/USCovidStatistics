package com.example.uscovidstatistics.views.activities.usabase

import android.content.Context
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.manualdependency.DependencyInjectorImpl
import com.example.uscovidstatistics.model.DataModelRepository
import com.example.uscovidstatistics.model.apidata.BaseCountryDataset
import com.example.uscovidstatistics.model.apidata.JhuCountryDataset
import com.example.uscovidstatistics.model.apidata.JhuProvinceDataset
import com.example.uscovidstatistics.model.apidata.StateDataset
import com.example.uscovidstatistics.network.NetworkRequests
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Response
import java.lang.Exception
import java.lang.reflect.ParameterizedType
import java.util.ArrayList

class UsaPresenter(view: UsaContract.View, dependencyInjectorImpl: DependencyInjectorImpl) : UsaContract.Presenter {

    private var view: UsaContract.View? = view

    private val dataModelRepository: DataModelRepository = dependencyInjectorImpl.covidDataRepository()

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    override fun onDestroy() {
        this.view = null
    }

    override fun onViewCreated() {
        loadStateData(AppConstants.DATA_SPECIFICS)
    }

    private fun loadStateData(getSpecifics: Int) {
        Observable.defer {
            try {
                val networkRequests = NetworkRequests(getSpecifics, null, null).getLocationData()
                Observable.just(networkRequests)
            } catch (e: Exception) {
                Observable.error<Exception>(e)
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe (
                {onNext -> onNext as Response
                    setData(onNext, getSpecifics)},
                {onError -> view?.dataError(onError)},
                {}
            )
    }

    private fun setData(response: Response, getSpecifics: Int) {
        val body = response.body!!
        val type: ParameterizedType = Types.newParameterizedType(List::class.java, StateDataset::class.java)
        try {
            val jsonAdapter: JsonAdapter<ArrayList<StateDataset>> = moshi.adapter(type)
            AppConstants.US_DATA = jsonAdapter.fromJson(body.string())!!
            for (data in AppConstants.US_DATA) {
                AppConstants.US_STATE_DATA_MAPPED[data.state!!] = data
            }
        } catch (e: Exception) {
            view?.dataError(e)
            e.printStackTrace()
        }
        body.close()
    }

    override fun getStateData(regionList: Array<String>) {
        TODO("Not yet implemented")
    }

    override fun onServiceStarted(context: Context) {
        TODO("Not yet implemented")
    }
}