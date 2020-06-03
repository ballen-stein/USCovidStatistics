package com.example.uscovidstatistics.network

import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.apidata.BaseCountryDataset
import com.example.uscovidstatistics.model.apidata.ContinentDataset
import com.example.uscovidstatistics.model.apidata.StateDataset
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.lang.reflect.ParameterizedType

class NetworkRequests(getSpecifics: Int, regionName: String?, countryName: String?) {
    private var useUrl: String = when (getSpecifics) {
        0 -> AppConstants.API_DATA_URL_GLOBAL
        1 -> AppConstants.API_DATA_URL_USA
        2 -> AppConstants.API_DATA_URL_USA_STATE + regionName + AppConstants.API_DATA_ENDPOINT
        3 -> AppConstants.API_DATA_CONTINENT
        4 -> AppConstants.API_DATA_JHU_COUNTRY + countryName + AppConstants.API_DATA_JHU_ENDPOINT
        5 -> AppConstants.API_DATA_JHU_COUNTRY + countryName + "/" + regionName + AppConstants.API_DATA_JHU_ENDPOINT
        else ->  AppConstants.API_DATA_URL_GLOBAL
    }
    private val client = OkHttpClient()

    fun getLocationData(): Response {
        val request = Request.Builder()
            .url(useUrl)
            .build()
        return client.newCall(request).execute()
    }
}