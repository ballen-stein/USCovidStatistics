package com.example.uscovidstatistics.network

import com.example.uscovidstatistics.appconstants.AppConstants
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class NetworkRequests(getSpecifics: Int, regionName: String?, countryName: String?) {

    private var useUrl: String = when (getSpecifics) {
        0 -> AppConstants.Api_Data_Url_Global
        1 -> AppConstants.Api_Data_Url_Usa
        2 -> AppConstants.Api_Data_Url_Usa + "/$regionName${AppConstants.Api_Data_Endpoint}"
        3 -> AppConstants.Api_Data_Continent
        4 -> AppConstants.Api_Data_Jhu_Country + "/$countryName${AppConstants.Api_Data_Jhu_Endpoint_7}"
        5 -> AppConstants.Api_Data_Jhu_Country + "/$countryName/$regionName${AppConstants.Api_Data_Jhu_Endpoint_All}"
        6 -> AppConstants.Api_Data_Jhu
        7 -> "${AppConstants.Api_Data_Url_Global}/$countryName"
        else ->  AppConstants.Api_Data_Url_Global
    }

    private val client = OkHttpClient().newBuilder().build()

    fun getLocationData(): Response {
        val request = Request.Builder()
            .url(useUrl)
            .build()

        return client.newCall(request).execute()
    }
}