package com.example.uscovidstatistics.network

import android.widget.Toast
import com.example.uscovidstatistics.MainActivity
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.BaseCountryDataset
import com.example.uscovidstatistics.model.StateDataset
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

class NetworkObserver(private var usingLocation: Boolean, private val activity: MainActivity) {
    private var useUrl: String
    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private lateinit var type: ParameterizedType
    private lateinit var response: Response

    init {
        if (usingLocation) {
            type = Types.newParameterizedType(List::class.java, StateDataset::class.java)
            useUrl = AppConstants.API_DATA_URL_USA
        }
        else {
            type = Types.newParameterizedType(List::class.java, BaseCountryDataset::class.java)
            useUrl = AppConstants.API_DATA_URL_GLOBAL
        }
    }

    fun createNewNetworkRequest() {
        Observable.defer {
            try {
                val apiResponse = if (usingLocation)
                    getLocationData()
                else
                    getAllData()
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
                }
            )
    }

    private fun setData(body: ResponseBody) {
        try {
            if (usingLocation) {
                val jsonAdapter: JsonAdapter<List<StateDataset>> = moshi.adapter(type)
                AppConstants.US_DATA = jsonAdapter.fromJson(body.string())!!
                for (data in AppConstants.US_DATA) {
                    AppConstants.US_STATE_DATA[data.state!!] = data
                }
            }
            else {
                val jsonAdapter: JsonAdapter<List<BaseCountryDataset>> = moshi.adapter(type)
                AppConstants.WORLD_DATA = jsonAdapter.fromJson(body.string())!!
            }
        } catch (e: Exception) {
            Toasty.info(activity.applicationContext, R.string.no_connection, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun getAllData(): Response {
        val request = Request.Builder()
            .url(useUrl)
            .build()
        return client.newCall(request).execute()
    }

    private fun getLocationData(): Response {
        val request = Request.Builder()
            .url(useUrl)
            .build()
        return client.newCall(request).execute()
    }
}