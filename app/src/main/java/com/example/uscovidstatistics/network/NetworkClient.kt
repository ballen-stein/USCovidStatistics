package com.example.uscovidstatistics.network

import com.example.uscovidstatistics.appconstants.AppConstants
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class NetworkClient(private var location: String) {
    private val client = OkHttpClient()

    fun getData(usingLocation: Boolean): Response {
        return if (usingLocation)
            getLocationData()
        else
            getAllData()
    }

    private fun getAllData(): Response {
        val request = Request.Builder()
            .url(AppConstants.ALL_DATA)
            .build()
        return client.newCall(request).execute()
    }

    private fun getLocationData(): Response {
        val request = Request.Builder()
            .url(AppConstants.LOCATION_DATA + location)
            .build()
        return client.newCall(request).execute()
    }
}