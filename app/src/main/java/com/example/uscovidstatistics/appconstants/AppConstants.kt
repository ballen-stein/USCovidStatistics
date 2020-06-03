package com.example.uscovidstatistics.appconstants

import com.example.uscovidstatistics.DataResponseListener
import com.example.uscovidstatistics.model.*
import com.example.uscovidstatistics.model.apidata.*
import okhttp3.ResponseBody

class AppConstants {
    companion object {
        // Intents
        const val REQUEST_GPS_LOCATION = 101
        const val CHANNEL_ID = "channel_id_01"

        // Basic Data
        var APP_OPEN = false
        var DATA_SPECIFICS = 0
        var TIMER_DELAY = 0L

        // Listener
        var dataResponseListener: DataResponseListener? = null

        // API Urls
        const val API_DATA_URL_GLOBAL = "https://corona.lmao.ninja/v2/countries"
        const val API_DATA_URL_USA = "https://corona.lmao.ninja/v2/states"
        // https://corona.lmao.ninja/v2/states/<State>
        const val API_DATA_URL_USA_STATE = "https://corona.lmao.ninja/v2/states/"
        const val API_DATA_ENDPOINT = "?yesterday=falsecountries&sort"

        const val API_DATA_CONTINENT = "https://corona.lmao.ninja/v2/continents/?yesterday=falsecountries&sort"

        // John Hopkins University, used for non-US countries and breakdowns for Provinces/Counties/etc
        const val API_DATA_JHU = "https://corona.lmao.ninja/v2/jhucsse"
        // "https://corona.lmao.ninja/v2/historical/<Country>"
        const val API_DATA_JHU_COUNTRY = "https://corona.lmao.ninja/v2/historical/"
        // "https://corona.lmao.ninja/v2/historical/<Country>/<Province>"
        const val API_DATA_JHU_PROVINCE = "https://corona.lmao.ninja/v2/historical/"
        const val API_DATA_JHU_ENDPOINT = "?lastdays=all"

        const val CURRENT_GPS_LOCATION = "current_location"

        // Global data
        val GPS_DATA = DoubleArray(2)
        lateinit var LOCATION_DATA: LocationDataset
        lateinit var RESPONSE_DATA: ResponseBody

        lateinit var WORLD_DATA: List<BaseCountryDataset>
        var WORLD_DATA_MAPPED: HashMap<String, BaseCountryDataset> = HashMap()

        lateinit var US_DATA: List<StateDataset>
        lateinit var US_STATE_DATA: StateDataset
        var US_STATE_DATA_MAPPED: HashMap<String, StateDataset> = HashMap()

        lateinit var CONTINENT_DATA: List<ContinentDataset>
        lateinit var COUNTRY_DATA: JhuCountryDataset
        lateinit var COUNTRY_PROVINCE_DATA: JhuProvinceDataset
    }
}