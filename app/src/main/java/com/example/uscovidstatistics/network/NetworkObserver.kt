package com.example.uscovidstatistics.network

import com.example.uscovidstatistics.MainActivity
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.BaseCountryDataSet
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.lang.Exception

class NetworkObserver(private var usingLocation: Boolean, specificLocation: String, private val activity: MainActivity) {
    private var useUrl: String
    private val client = OkHttpClient()
    private lateinit var response: Response

    init {
        useUrl = if (usingLocation)
            AppConstants.API_DATA_BY_US_STATE
        else
            AppConstants.API_DATA_BY_COUNTRY
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
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, BaseCountryDataSet::class.java)
        val jsonAdapter: JsonAdapter<List<BaseCountryDataSet>> = moshi.adapter(type)

        try {
            AppConstants.WORLD_DATA = jsonAdapter.fromJson(body.string())!!
            println("Data set is valid")
        } catch (e: Exception) {
            println("Data set is invalid")
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