package com.example.uscovidstatistics.appconstants

import okhttp3.Response
import okhttp3.ResponseBody

class AppConstants {
    companion object {
        const val REQUEST_GPS_LOCATION = 101

        const val ALL_DATA = "https://api.covid19api.com/total/country/united-states"
        const val LOCATION_DATA = ""

        lateinit var RESPONSE_DATA: ResponseBody
    }
}