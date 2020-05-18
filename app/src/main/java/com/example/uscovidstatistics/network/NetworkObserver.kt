package com.example.uscovidstatistics.network

import android.app.Activity
import com.example.uscovidstatistics.MainActivity
import com.example.uscovidstatistics.appconstants.AppConstants
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.lang.Exception

class NetworkObserver(private var usingLocation: Boolean, specificLocation: String, private val activity: MainActivity) {
    private var useUrl: String
    private val client = OkHttpClient()
    private lateinit var response: Response

    init {
        useUrl = if (usingLocation)
            AppConstants.LOCATION_DATA + "/" + specificLocation
        else
            AppConstants.ALL_DATA
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
            .subscribe(
                { onNext -> response = onNext as Response},
                { onError -> println(onError.toString() + " onError area")},
                { AppConstants.RESPONSE_DATA = response.body!!
                    activity.printDataSet()}
            )
    }

    fun getData(specificLocation: Boolean): Response {
        useUrl = if (specificLocation)
            AppConstants.LOCATION_DATA + "/"
        else
            AppConstants.ALL_DATA

        return NetworkClient(useUrl).getData(specificLocation)
    }

    fun getAllData(): Response {
        val request = Request.Builder()
            .url(useUrl)
            .build()
        return client.newCall(request).execute()
    }

    fun getLocationData(): Response {
        val request = Request.Builder()
            .url(useUrl)
            .build()
        return client.newCall(request).execute()
    }
}