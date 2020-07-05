package com.example.uscovidstatistics.views.activities.state

import android.content.Context
import android.util.Log
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.manualdependency.DependencyInjectorImpl
import com.example.uscovidstatistics.model.DataModelRepository
import com.example.uscovidstatistics.model.apidata.StateDataset
import com.example.uscovidstatistics.network.NetworkRequests
import com.example.uscovidstatistics.utils.AppUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Response
import java.lang.Exception
import java.net.UnknownHostException
import java.util.*

class StatePresenter(view: StateContract.View, dependencyInjectorImpl: DependencyInjectorImpl) : StateContract.Presenter{

    private val dataModelRepository: DataModelRepository = dependencyInjectorImpl.covidDataRepository()

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private var view: StateContract.View? = view

    private val appUtils = AppUtils.getInstance()

    private val timer = Timer()

    override fun onDestroy() {
        this.view = null
    }

    override fun onViewCreated(mContext: Context) {
        if (appUtils.checkNetwork(mContext)) {
            Log.d("CovidTesting", "Yes network")
            loadData()
        } else {
            Log.d("CovidTesting", "No network")
            view?.dataError(UnknownHostException())
        }
    }

    private fun loadData() {
        Observable.defer {
            try {
                val networkRequests = NetworkRequests(AppConstants.Data_Specifics, AppConstants.Region_Name, AppConstants.Country_Name).getLocationData()
                Observable.just(networkRequests)
            } catch (e: Exception) {
                Observable.error<Exception>(e)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { onNext -> onNext as Response
                    setData(onNext)
                },
            { onError -> view?.dataError(onError)},
            { view?.displayStateData(dataModelRepository.getUsState()) }
            )
    }

    private fun setData(response: Response) {
        val body = response.body!!
        try {
            val jsonAdapter = moshi.adapter(StateDataset::class.java)
            AppConstants.Us_State_Data = jsonAdapter.fromJson(body.string())!!
        } catch (e: Exception) {
            view?.dataError(e)
        }
        body.close()
    }

    override fun getStateData(): List<StateDataset> {
        return dataModelRepository.getUsData()
    }

    override fun networkStatus(mContext: Context) {
        appUtils.restoreNetwork(mContext)
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (appUtils.checkNetwork(mContext) && AppConstants.Wifi_Check) {
                    this.cancel()
                    view?.onResumeData()
                    AppConstants.Wifi_Check = false
                }
            }
        }, 0, 500)
    }
}