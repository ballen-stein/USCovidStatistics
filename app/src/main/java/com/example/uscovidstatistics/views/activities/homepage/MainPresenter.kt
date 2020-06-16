package com.example.uscovidstatistics.views.activities.homepage

import android.content.Context
import android.os.Looper
import android.util.Log
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.manualdependency.DependencyInjector
import com.example.uscovidstatistics.model.DataModelRepository
import com.example.uscovidstatistics.model.apidata.*
import com.example.uscovidstatistics.network.NetworkRequests
import com.example.uscovidstatistics.utils.AppUtils
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
import java.util.*
import javax.inject.Inject

class MainPresenter @Inject constructor(view: MainContract.View, dependencyInjector: DependencyInjector) : MainContract.Presenter {

    private val dataModelRepository: DataModelRepository = dependencyInjector.covidDataRepository()

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private var view: MainContract.View? = view

    override fun onDestroy() {
        this.view = null
    }

    override fun onViewCreated() {
        loadData(AppConstants.DATA_SPECIFICS)
    }

    override fun onDataUpdated() {
        Log.d("CovidTesting", "Data was updated . . .")
    }

    override fun onServiceStarted(context: Context) {
        val timer = Timer()
        timer.schedule(object: TimerTask() {
            override fun run() {
                Log.d("CovidTesting","Running service . . .")
                Thread(Runnable {
                    Looper.prepare()
                    loadData(3)
                }).start()
            }
        },0, 2*60*1000)
    }

    private fun loadData(getSpecifics: Int) {
        Observable.defer {
            try {
                val networkRequests = NetworkRequests(getSpecifics, null, null).getLocationData()
                Observable.just(networkRequests)
            } catch (e: Exception) {
                Observable.error<Exception>(e)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { onNext -> onNext as Response
                    setData(onNext, getSpecifics)},
                { onError ->  view?.dataError()
                    Log.d("CovidTesting", "Error in the subscription : $onError")},
                { view?.displayContinentData(AppUtils().continentTotals(dataModelRepository.getContinentData())) }
            )
    }

    private fun setData(response: Response, getSpecifics: Int) {
        val body = response.body!!
        val type: ParameterizedType = Types.newParameterizedType(
            when (getSpecifics) {
                0,1,3 -> List::class.java
                else -> List::class.java
            },
            when (getSpecifics) {
                0 -> BaseCountryDataset::class.java
                1,2 -> StateDataset::class.java
                3 -> ContinentDataset::class.java
                else -> BaseCountryDataset::class.java
            })

        try {
            when (getSpecifics) {
                0 -> {
                    val jsonAdapter: JsonAdapter<List<BaseCountryDataset>> = moshi.adapter(type)
                    AppConstants.WORLD_DATA = jsonAdapter.fromJson(body.string())!!
                }
                1 -> {
                    val jsonAdapter: JsonAdapter<ArrayList<StateDataset>> = moshi.adapter(type)
                    AppConstants.US_DATA = jsonAdapter.fromJson(body.string())!!
                    for (data in AppConstants.US_DATA) {
                        AppConstants.US_STATE_DATA_MAPPED[data.state!!] = data
                    }
                }
                2 -> { // Type isn't used since JSON data is not a list
                    val jsonAdapter = moshi.adapter(StateDataset::class.java)
                    AppConstants.US_STATE_DATA = jsonAdapter.fromJson(body.string())!!
                }
                3 -> {
                    val jsonAdapter: JsonAdapter<List<ContinentDataset>> = moshi.adapter(type)
                    AppConstants.CONTINENT_DATA = jsonAdapter.fromJson(body.string())!!
                    /*for (data in AppConstants.CONTINENT_DATA) {
                        AppConstants.CONTINENT_COUNTRY_LIST[data.continent!!] = data.countriesOnContinent
                    }*/
                }
                4 -> { // Type isn't used since JSON data is not a list
                    val jsonAdapter = moshi.adapter(JhuCountryDataset::class.java)
                    AppConstants.COUNTRY_DATA = jsonAdapter.fromJson(body.string())!!
                }
                5 -> { // Type isn't used since JSON data is not a list
                    val jsonAdapter = moshi.adapter(JhuProvinceDataset::class.java)
                    AppConstants.COUNTRY_PROVINCE_DATA = jsonAdapter.fromJson(body.string())!!
                }
                else -> {
                    val jsonAdapter: JsonAdapter<List<BaseCountryDataset>> = moshi.adapter(type)
                    AppConstants.WORLD_DATA = jsonAdapter.fromJson(body.string())!!
                }
            }
        } catch (e: Exception) {
            view?.dataError()
            e.printStackTrace()
        }
        body.close()
    }

}