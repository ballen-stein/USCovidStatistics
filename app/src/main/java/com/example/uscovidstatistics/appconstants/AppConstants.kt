package com.example.uscovidstatistics.appconstants

import android.content.SharedPreferences
import com.example.uscovidstatistics.model.*
import com.example.uscovidstatistics.model.apidata.*
import io.reactivex.rxjava3.subjects.PublishSubject

class AppConstants {
    companion object {
        // Intents
        const val CHANNEL_ID = "channel_id_01"
        const val DISPLAY_GPS = "display_gps_location"
        const val DISPLAY_COUNTRY = "display_country_location"
        const val DISPLAY_REGION = "display_region_location"
        const val LOAD_STATE = "load_us_state"
        const val REQUEST_GPS_LOCATION = 101

        // Basic Data
        var APP_OPEN = false
        var COUNTRY_NAME: String? = null
        var DATA_SPECIFICS = 3
        val NON_CLICK_IDS = arrayOf("Other", "Territories", "Totals", "States & DC")
        var PREFERENCE_CHECK = false
        var REGION_NAME: String? = null
        var RECYCLER_CLICKABLE = true
        var Saved_Locations: ArrayList<String> = ArrayList()
        var Settings_Updated = false
        var Timer_Delay = 0L
        var Update_Frequency = 5L
        var Updating_Global = false
        var Updating_Country = false
        var Usa_Check = false

        // User Preferences
        lateinit var USER_PREFS: SharedPreferences

        // Service Data
        var GLOBAL_SERVICE_ON = false
        var COUNTRY_SERVICE_ON = false
        var REGIONAL_SERVICE_ON = false

        // API Urls
        const val API_DATA_URL_GLOBAL = "https://corona.lmao.ninja/v2/countries"
        const val API_DATA_URL_USA = "https://corona.lmao.ninja/v2/states"
        const val API_DATA_ENDPOINT = "?yesterday=true"
        const val API_DATA_CONTINENT = "https://corona.lmao.ninja/v2/continents/?yesterday=falsecountries&sort"
        const val API_DATA_JHU = "https://corona.lmao.ninja/v2/jhucsse"
        const val API_DATA_JHU_COUNTRY = "https://corona.lmao.ninja/v2/historical"
        const val API_DATA_JHU_ENDPOINT_7 = "?lastdays=7"
        const val API_DATA_JHU_ENDPOINT_ALL = "?lastdays=all"

        const val CURRENT_GPS_LOCATION = "current_location"

        // App-Global Covid Data Models
        val GPS_DATA = DoubleArray(2)
        lateinit var LOCATION_DATA: LocationDataset

        lateinit var WORLD_DATA: List<BaseCountryDataset>
        var WORLD_DATA_MAPPED: HashMap<String, BaseCountryDataset> = HashMap()

        lateinit var REGIONAL_DATA: List<JhuBaseDataset>

        lateinit var US_DATA: ArrayList<StateDataset>
        lateinit var US_STATE_DATA: StateDataset
        var US_STATE_DATA_MAPPED: HashMap<String, StateDataset> = HashMap()

        lateinit var CONTINENT_DATA: List<ContinentDataset>

        lateinit var COUNTRY_DATA: JhuCountryDataset
        lateinit var COUNTRY_PROVINCE_DATA: JhuProvinceDataset
        var COUNTRY_PROVINCE_LIST: ArrayList<JhuProvinceDataset> = ArrayList()
    }
}