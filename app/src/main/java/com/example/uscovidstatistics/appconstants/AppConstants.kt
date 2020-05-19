package com.example.uscovidstatistics.appconstants

import com.example.uscovidstatistics.model.BaseCountryDataSet
import com.example.uscovidstatistics.model.LocationDataSet
import okhttp3.ResponseBody

class AppConstants {
    companion object {
        // Intents
        const val REQUEST_GPS_LOCATION = 101

        // Strings
        const val API_DATA_ALL = "https://api.covid19api.com/total/country/united-states"
        const val API_DATA_LOCATION = ""
        const val CURRENT_GPS_LOCATION = "current_location"

        const val API_DATA_BY_COUNTRY = "https://corona.lmao.ninja/v2/countries?yesterday&sort"
        const val API_DATA_BY_US_STATE = "https://corona.lmao.ninja/v2/states?sort&yesterday"
        //const val API_DATA_US_STATE = "https://corona.lmao.ninja/v2/jhucsse"
        //const val API_DATA_WORLD = "https://corona.lmao.ninja/v2/countries?yesterday&sort"

        // Global data
        val GPS_DATA = DoubleArray(2)
        lateinit var LOCATION_DATA: LocationDataSet
        lateinit var RESPONSE_DATA: ResponseBody
        lateinit var WORLD_DATA: List<BaseCountryDataSet>
    }
}