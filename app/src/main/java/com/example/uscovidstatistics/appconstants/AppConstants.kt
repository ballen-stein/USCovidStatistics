package com.example.uscovidstatistics.appconstants

import android.content.SharedPreferences
import com.example.uscovidstatistics.model.*
import com.example.uscovidstatistics.model.apidata.*
import io.reactivex.rxjava3.subjects.PublishSubject

class AppConstants {
    companion object {
        // Intents
        const val Channel_Id = "channel_id_01"
        const val Display_Gps = "display_gps_location"
        const val Display_Country = "display_country_location"
        const val Display_Region = "display_region_location"
        const val Load_State = "load_us_state"
        const val Request_Gps_Location = 101

        // Basic Data
        var App_Open = false
        var Country_Name: String? = null
        var Data_Specifics = 3
        val Non_Click_Ids = arrayOf("Other", "Territories", "Totals", "States & DC")
        var Preference_Check = false
        var Region_Name: String? = null
        var Recycler_Clickable = true
        var Saved_Locations: ArrayList<String> = ArrayList()
        var Settings_Updated = false
        var Timer_Delay = 0L
        var Update_Frequency = 5L
        var Updating_Global = false
        var Updating_Country = false
        var Usa_Check = false

        // User Preferences
        lateinit var User_Prefs: SharedPreferences

        // Service Data
        var Global_Service_On = false
        var Country_Service_On = false
        var Notification_Service_On = true
        var Regional_Service_On = false

        // API Urls
        const val Api_Data_Url_Global = "https://corona.lmao.ninja/v2/countries"
        const val Api_Data_Url_Usa = "https://corona.lmao.ninja/v2/states"
        const val Api_Data_Endpoint = "?yesterday=true"
        const val Api_Data_Continent = "https://corona.lmao.ninja/v2/continents/?yesterday=falsecountries&sort"
        const val Api_Data_Jhu = "https://corona.lmao.ninja/v2/jhucsse"
        const val Api_Data_Jhu_Country = "https://corona.lmao.ninja/v2/historical"
        const val Api_Data_Jhu_Endpoint_7 = "?lastdays=7"
        const val Api_Data_Jhu_Endpoint_All = "?lastdays=all"

        const val Current_Gps_Location = "current_location"

        // App-Global Covid Data Models
        val Gps_Data = DoubleArray(2)
        lateinit var Location_Data: LocationDataset

        lateinit var World_Data: List<BaseCountryDataset>
        var World_Data_Mapped: HashMap<String, BaseCountryDataset> = HashMap()

        lateinit var Regional_Data: List<JhuBaseDataset>

        lateinit var Us_Data: ArrayList<StateDataset>
        lateinit var Us_State_Data: StateDataset
        var Us_State_Data_Mapped: HashMap<String, StateDataset> = HashMap()

        lateinit var Continent_Data: List<ContinentDataset>

        lateinit var Country_Data: JhuCountryDataset
        lateinit var Country_Province_Data: JhuProvinceDataset
        var Country_Province_List: ArrayList<JhuProvinceDataset> = ArrayList()
    }
}