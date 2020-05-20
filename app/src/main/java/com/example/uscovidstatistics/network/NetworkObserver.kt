package com.example.uscovidstatistics.network

import android.util.Log
import android.widget.Toast
import com.example.uscovidstatistics.MainActivity
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import es.dmoral.toasty.Toasty
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.lang.Exception
import java.lang.reflect.ParameterizedType

class NetworkObserver(private val activity: MainActivity, private var getSpecifics: Int, countryName: String?, regionName: String?) {
    private var useUrl: String
    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private var type: ParameterizedType
    private lateinit var response: Response

    init {

        type = Types.newParameterizedType(
            when (getSpecifics) {
                0,1,3 -> List::class.java
                else -> List::class.java
            },
            when (getSpecifics) {
                0 -> BaseCountryDataset::class.java
                1,2 -> StateDataset::class.java
                3 -> ContinentDataset::class.java
                4 -> JhuCountryDataset::class.java
                5 -> JhuProvinceDataset::class.java
                else -> BaseCountryDataset::class.java
            })

        useUrl = when (getSpecifics) {
            0 -> AppConstants.API_DATA_URL_GLOBAL
            1 -> AppConstants.API_DATA_URL_USA
            2 -> AppConstants.API_DATA_URL_USA_STATE
            3 -> AppConstants.API_DATA_CONTINENT
            4 -> AppConstants.API_DATA_JHU_COUNTRY + countryName
            5 -> AppConstants.API_DATA_JHU_COUNTRY + countryName + "/" + regionName

            else ->  AppConstants.API_DATA_URL_GLOBAL
        }
    }

    fun createNewNetworkRequest() {
        Observable.defer {
            try {
                val apiResponse = getLocationData()
                Observable.just(apiResponse)
            } catch (e: Exception) {
                Observable.error<Exception>(e)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onNext -> response = onNext as Response
                    setData(response.body!!)},
                { onError -> println(onError.toString())},
                { activity.printDataSet()
                    response.close()
                }
            )
    }

    private fun setData(body: ResponseBody) {

        try {
            when (getSpecifics) {
                0 -> {
                    val jsonAdapter: JsonAdapter<List<BaseCountryDataset>> = moshi.adapter(type)
                    AppConstants.WORLD_DATA = jsonAdapter.fromJson(body.string())!!
                    //Log.d("NetworkUrl", body.string())
                }
                1 -> {
                    val jsonAdapter: JsonAdapter<List<StateDataset>> = moshi.adapter(type)
                    AppConstants.US_DATA = jsonAdapter.fromJson(body.string())!!
                    for (data in AppConstants.US_DATA) {
                        AppConstants.US_STATE_DATA_MAPPED[data.state!!] = data
                    }
                }
                2 -> {//
                    val jsonAdapter = moshi.adapter(StateDataset::class.java)
                    AppConstants.US_STATE_DATA = jsonAdapter.fromJson(body.string())!!
                }
                3 -> {
                    val jsonAdapter: JsonAdapter<List<ContinentDataset>> = moshi.adapter(type)
                    AppConstants.CONTINENT_DATA = jsonAdapter.fromJson(body.string())!!
                }
                4 -> {//
                    val jsonAdapter = moshi.adapter(JhuCountryDataset::class.java)
                    AppConstants.COUNTRY_DATA = jsonAdapter.fromJson(body.string())!!
                    Log.d("NetworkUrl", body.string())
                }
                5 -> {//
                    val jsonAdapter = moshi.adapter(JhuProvinceDataset::class.java)
                    AppConstants.COUNTRY_PROVINCE_DATA = jsonAdapter.fromJson(body.string())!!
                }
            }
        } catch (e: Exception) {
            Toasty.info(activity.applicationContext, R.string.no_connection, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun getLocationData(): Response {
        val request = Request.Builder()
            .url(useUrl)
            .build()
        Log.d("NetworkUrl", useUrl)
        return client.newCall(request).execute()
    }
}