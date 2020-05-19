package com.example.uscovidstatistics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.uscovidstatistics.appconstants.AppConstants
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

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivityTag"
    private lateinit var response: Response
    private var gpsCords: DoubleArray? = DoubleArray(2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gpsCords = intent.getDoubleArrayExtra(AppConstants.CURRENT_GPS_LOCATION)

        /*

        Observable.defer {
            try {
                val apiResponse = AppConstants.RESPONSE_DATA
                Observable.just(apiResponse)
            } catch (e: Exception) {
                println("Error putting observer on Response_Data")
                Observable.error<Exception>(e)
            }
        }.subscribeOn(Schedulers.io())
            .subscribe(
                { onNext -> println("onNext : " + onNext)},
                { onError -> println("onError : " + onError)},
                { println("Completed the observer")}
            )

         */

        Thread(Runnable {
            NetworkObserver(false, this).createNewNetworkRequest()
            NetworkObserver(true, this).createNewNetworkRequest()
        }).start()

        Handler().postDelayed(
            {
                for (data in AppConstants.WORLD_DATA)
                    AppConstants.WORLD_DATA_MAPPED[data.country!!] = data
                println(AppConstants.US_DATA[0].state)
                println("${AppConstants.WORLD_DATA_MAPPED["Germany"]!!.country} had ${AppConstants.WORLD_DATA_MAPPED["Germany"]!!.deaths} deaths and ${AppConstants.WORLD_DATA_MAPPED["Germany"]!!.recovered} recovered")
                println("${AppConstants.US_STATE_DATA["New York"]!!.state} had ${AppConstants.US_STATE_DATA["New York"]!!.deaths} deaths")
                if (AppUtils().gpsPermissionGranted(this)) {
                    println("${AppConstants.US_STATE_DATA[AppConstants.LOCATION_DATA.region]!!.state}")
                }
            }
            , 8000
        )

        //Observable.just(AppConstants.RESPONSE_DATA).subscribe({ onNext -> println(onNext)})

        /*
        Observable.defer {
            try {
                val apiResponse = NetworkObserver(false, "").getAllData()
                Observable.just(apiResponse)
            } catch (e: Exception) {
                Observable.error<Exception>(e)
            }
        }.subscribeOn(Schedulers.io())
            .subscribe(
                { onNext -> response = onNext as Response},
                { onError -> println(onError.toString() + " onError area")},
                { AppConstants.RESPONSE_DATA = response.body!!
                    println("New Response : ${AppConstants.RESPONSE_DATA.string()}")}
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
        try {
            println(AppConstants.US_DATA[0].state)
        } catch (e: Exception) {
            println(AppConstants.WORLD_DATA[0].country)
        }
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
}
