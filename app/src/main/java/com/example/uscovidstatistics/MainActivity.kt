package com.example.uscovidstatistics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.ActivityMainBinding
import com.example.uscovidstatistics.model.BaseCountryDataset
import com.example.uscovidstatistics.model.LocationDataset
import com.example.uscovidstatistics.network.NetworkObserver
import com.example.uscovidstatistics.utils.AppUtils
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Response
import java.lang.Exception

class MainActivity : AppCompatActivity(), ViewBinding {
    private val TAG = "MainActivityTag"
    private lateinit var response: Response
    private var gpsCords: DoubleArray? = DoubleArray(2)

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        //setContentView(R.layout.activity_main)
        setContentView(view)

        gpsCords = intent.getDoubleArrayExtra(AppConstants.CURRENT_GPS_LOCATION)

        Thread(Runnable {
            //NetworkObserver(this, 0, null, null).createNewNetworkRequest()
            //NetworkObserver(this, 1, null, null).createNewNetworkRequest()
            //NetworkObserver(this, 2, null, "California").createNewNetworkRequest()
            NetworkObserver(this, 3, null, null).createNewNetworkRequest()
            //NetworkObserver(this, 4, "Canada", null).createNewNetworkRequest()
            //NetworkObserver(this, 5, "Canada", "ontario").createNewNetworkRequest()
        }).start()

        Handler().postDelayed( {
            val dataArray = AppUtils().totalGlobalCases()

            binding.globalCases.text = AppUtils().formatNumbers(dataArray[0])
            binding.globalRecovered.text = AppUtils().formatNumbers(dataArray[1])
            binding.globalDeaths.text = AppUtils().formatNumbers(dataArray[2])

            binding.currentInfected.text = AppUtils().formatNumbers(dataArray[3])
            val mildText = AppUtils().formatNumbers(dataArray[4]) + " (${AppUtils().getStringPercent(dataArray[4], dataArray[3])}%)"
            binding.currentMild.text = mildText
            val criticalText = AppUtils().formatNumbers(dataArray[5]) + " (${AppUtils().getStringPercent(dataArray[5], dataArray[3])}%)"
            binding.currentCritical.text = criticalText

            binding.currentClosed.text = AppUtils().formatNumbers(dataArray[6])
            binding.currentDischarged.text = binding.globalRecovered.text
            binding.currentDead.text = binding.globalDeaths.text
        }, 10000)

        /*

        Handler().postDelayed(
            {
                // Check for 0
                for (data in AppConstants.WORLD_DATA)
                    AppConstants.WORLD_DATA_MAPPED[data.country!!] = data

                println("${AppConstants.WORLD_DATA_MAPPED["Germany"]!!.country} had ${AppConstants.WORLD_DATA_MAPPED["Germany"]!!.deaths} deaths and ${AppConstants.WORLD_DATA_MAPPED["Germany"]!!.recovered} recovered")

                // Check for 1
                println(AppConstants.US_DATA[0].state)
                println("${AppConstants.US_STATE_DATA_MAPPED["New York"]!!.state} had ${AppConstants.US_STATE_DATA_MAPPED["New York"]!!.deaths} deaths")

                // GPS Check (coincides with 1)
                if (AppUtils().gpsPermissionGranted(this)) {
                    println("${AppConstants.US_STATE_DATA_MAPPED[AppConstants.LOCATION_DATA.region]!!.state}")
                }

                // Check for 2
                println("${AppConstants.US_STATE_DATA.state} has had ${AppConstants.US_STATE_DATA.deaths} as of today")

                // Check for 3
                var globalTotal: Int = 0
                var globalDeaths: Int = 0
                for (continent in AppConstants.CONTINENT_DATA) {
                    println(continent.continent)
                    println(continent.countriesOnContinent?.contentToString())
                    globalTotal += continent.cases!!
                    globalDeaths += continent.deaths!!
                }
                println("Global Cases : $globalTotal and Global Deaths : $globalDeaths")

                // Check for 4
                println("${AppConstants.COUNTRY_DATA.country}")
                for (province in AppConstants.COUNTRY_DATA.province!!) {
                    println(province)
                }

                // Check for 5
                val allKeys: ArrayList<String> = ArrayList()
                for (key in AppConstants.COUNTRY_PROVINCE_DATA.timeline?.cases?.keys!!) {
                    allKeys.add(key)
                }
                println(AppConstants.COUNTRY_PROVINCE_DATA.timeline?.cases)
                println("${AppConstants.COUNTRY_PROVINCE_DATA.province} has had ${AppConstants.COUNTRY_PROVINCE_DATA.timeline!!.cases?.get(allKeys[allKeys.size-1])} cases on ${allKeys[allKeys.size-1]}")
            }
            , 60000
        )

         */

    }

    fun printDataSet() {
        println("Received dataset --- printing...")

        /*val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, BaseCountryDataSet::class.java)
        val jsonAdapter: JsonAdapter<List<BaseCountryDataSet>> = moshi.adapter(type)

        try {
            AppConstants.WORLD_DATA = jsonAdapter.fromJson(AppConstants.RESPONSE_DATA.string())!!
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
        /*try {
            println(AppConstants.US_DATA[0].state)
        } catch (e: Exception) {
            println(AppConstants.WORLD_DATA[0].country)
        }*/
    }

    private fun updateData() {
        // Moshi adapter code
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, BaseCountryDataset::class.java)
        val jsonAdapter: JsonAdapter<List<BaseCountryDataset>> = moshi.adapter(type)

        try {
            AppConstants.WORLD_DATA = jsonAdapter.fromJson(AppConstants.RESPONSE_DATA.string())!!
            println("Data set is valid")
        } catch (e: Exception) {
            println("Data set is invalid")
            e.printStackTrace()
        }
    }

    private fun printLocation() {
        try {
            val currentLocation: LocationDataset = AppUtils().getLocationData(this)
            Log.d("LocationInfo", currentLocation.toString())
        } catch (e: Exception) {
            Log.d("LocationInfo", "no location found")
            e.printStackTrace()
        }
    }

    override fun getRoot(): View {
        TODO("Not yet implemented")
    }
}
