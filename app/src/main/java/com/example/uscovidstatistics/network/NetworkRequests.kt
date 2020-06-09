package com.example.uscovidstatistics.network

import android.util.Log
import com.example.uscovidstatistics.appconstants.AppConstants
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class NetworkRequests(getSpecifics: Int, regionName: String?, countryName: String?) {

    //NetworkRequests(this, 0, null, null).createNewNetworkRequest()
    //NetworkRequests(this, 1, null, null).createNewNetworkRequest()
    //NetworkRequests(this, 2, null, "California").createNewNetworkRequest()
    //NetworkRequests(this, 3, null, null, true).createNewNetworkRequest()
    //NetworkRequests(this, 4, "Canada", null).createNewNetworkRequest()
    //NetworkRequests(this, 5, "Canada", "ontario").createNewNetworkRequest()

    private var useUrl: String = when (getSpecifics) {
        0 -> AppConstants.API_DATA_URL_GLOBAL
        1 -> AppConstants.API_DATA_URL_USA
        2 -> AppConstants.API_DATA_URL_USA + "/" + regionName + AppConstants.API_DATA_ENDPOINT
        3 -> AppConstants.API_DATA_CONTINENT
        4 -> AppConstants.API_DATA_JHU_COUNTRY + "/" + countryName + AppConstants.API_DATA_JHU_ENDPOINT
        5 -> AppConstants.API_DATA_JHU_COUNTRY + "/$countryName/$regionName"
        else ->  AppConstants.API_DATA_URL_GLOBAL
    }
    private val client = OkHttpClient()

    fun getLocationData(): Response {
        val request = Request.Builder()
            .url(useUrl)
            .build()

        Log.d("CovidTesting",useUrl)
        return client.newCall(request).execute()
    }
}