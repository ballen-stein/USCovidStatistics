package com.example.uscovidstatistics.views.activities.country

import android.content.Context
import android.content.Intent
import android.os.Looper
import android.util.Log
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.manualdependency.DependencyInjector
import com.example.uscovidstatistics.model.DataModelRepository
import com.example.uscovidstatistics.model.apidata.*
import com.example.uscovidstatistics.network.NetworkRequests
import com.example.uscovidstatistics.service.ScheduledService
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

class CountryPresenter(view: CountryContract.View, dependencyInjector: DependencyInjector, private val mContext: Context) : CountryContract.Presenter {

    private val dataModelRepository: DataModelRepository = dependencyInjector.covidDataRepository()

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private var view: CountryContract.View? = view

    private val timer = Timer()

    private val dataToIgnore = arrayOf("Territories", "Totals", "Others", "States & DC")

    override fun onDestroy() {
        this.view = null
        AppConstants.COUNTRY_SERVICE_ON = false
        mContext.stopService(Intent((mContext as CountryActivity), ScheduledService::class.java))
    }

    override fun onViewCreated(countryName: String) {
        if (AppConstants.USA_CHECK) {
            loadUsData(AppConstants.DATA_SPECIFICS, AppConstants.REGION_NAME, AppConstants.COUNTRY_NAME)
        } else {
            loadData(AppConstants.DATA_SPECIFICS, AppConstants.REGION_NAME, AppConstants.COUNTRY_NAME)
        }
    }

    private fun loadUsData(getSpecifics: Int, regionName: String?, countryName: String?) {
        Observable.defer {
            try {
                val networkRequests = NetworkRequests(getSpecifics, regionName, countryName).getLocationData()
                Observable.just(networkRequests)
            } catch (e: Exception) {
                Observable.error<Exception>(e)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { onNext -> onNext as Response
                    setData(onNext, getSpecifics, false) },
                { onError ->  view?.dataError(onError)
                    Log.d("CovidTesting", "Error in the subscription  for country : $onError")},
                { view?.displayUsList() }
            )
    }

    private fun loadData(getSpecifics: Int, regionName: String?, countryName: String?) {
        Observable.defer {
            try {
                val networkRequests = NetworkRequests(getSpecifics, regionName, countryName).getLocationData()
                Observable.just(networkRequests)
            } catch (e: Exception) {
                Observable.error<Exception>(e)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { onNext -> onNext as Response
                    setData(onNext, getSpecifics, false)},
                { onError ->  view?.dataError(onError)
                    Log.d("CovidTesting", "Error in the subscription  for country : $onError")},
                { view?.displayCountryData(dataModelRepository.getCountryData()) }
            )
    }

    private fun setData(response: Response, getSpecifics: Int, addToList: Boolean) {
        val body = response.body!!
        val type: ParameterizedType = Types.newParameterizedType(List::class.java,
            when (getSpecifics) {
                1 -> StateDataset::class.java
                else -> BaseCountryDataset::class.java
            })

        try {
            when (getSpecifics) {
                1 -> {
                    val jsonAdapter: JsonAdapter<ArrayList<StateDataset>> = moshi.adapter(type)
                    AppConstants.US_DATA = jsonAdapter.fromJson(body.string())!!
                    for (data in AppConstants.US_DATA) {
                        AppConstants.US_STATE_DATA_MAPPED[data.state!!] = data
                    }
                }
                4 -> { // Type isn't used since JSON data is not a list
                    val jsonAdapter = moshi.adapter(JhuCountryDataset::class.java)
                    AppConstants.COUNTRY_DATA = jsonAdapter.fromJson(body.string())!!
                }
                5 -> { // Type isn't used since JSON data is not a list
                    val jsonAdapter = moshi.adapter(JhuProvinceDataset::class.java)
                    AppConstants.COUNTRY_PROVINCE_DATA = jsonAdapter.fromJson(body.string())!!
                    if (addToList)
                        AppConstants.COUNTRY_PROVINCE_LIST.add(AppConstants.COUNTRY_PROVINCE_DATA)
                }
            }
        } catch (e: Exception) {
            view?.dataError(e)
            e.printStackTrace()
        }
        body.close()
    }

    override fun getRegionalData(getSpecifics: Int, regionList: Array<String>) {
        for (data in regionList) {
            if (dataToIgnore.contains(data)) {
                continue
            } else {
                Observable.defer {
                    try {
                        val networkRequests = NetworkRequests(getSpecifics, data, AppConstants.COUNTRY_NAME).getLocationData()
                        Observable.just(networkRequests)
                    } catch (e: Exception) {
                        Observable.error<Exception>(e)
                    }
                }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (
                        { onNext -> onNext as Response
                            setData(onNext, getSpecifics, true)},
                        { onError ->  view?.dataError(onError)
                            Log.d("CovidTesting", "Error in the subscription  for country : $onError")},
                        { if (AppConstants.COUNTRY_PROVINCE_LIST.size == regionList.size)
                            view?.displayCountryList()
                        }
                    )
            }
        }
    }

    override fun onServiceStarted(context: Context) {
        Log.d("CovidTesting", "Service for the country is currently running")
        timer.schedule(object: TimerTask() {
            override fun run() {
                Thread(Runnable {
                    Looper.prepare()
                    Log.d("CovidTesting", "Service getting data now . . .")
                    if (AppConstants.USA_CHECK) {
                        loadUsData(AppConstants.DATA_SPECIFICS, null, AppConstants.COUNTRY_NAME)
                    } else {
                        loadData(AppConstants.DATA_SPECIFICS, null, AppConstants.COUNTRY_NAME)
                    }
                }).start()
            }
        },0, 2*60*1000)
    }
}