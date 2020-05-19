package com.example.uscovidstatistics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.BaseCountryDataSet
import com.example.uscovidstatistics.model.LocationDataSet
import com.example.uscovidstatistics.network.NetworkObserver
import com.example.uscovidstatistics.utils.AppUtils
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Response
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivityTag"
    private lateinit var response: Response
    private var gpsCords: DoubleArray? = DoubleArray(2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gpsCords = intent.getDoubleArrayExtra(AppConstants.CURRENT_GPS_LOCATION)
        //printDataSet()

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

        Thread(Runnable {
            NetworkObserver(false, "", this).createNewNetworkRequest()
            //println("${AppConstants.RESPONSE_DATA} new resp")
            //printLocation()
        }).start()

        //Observable.just(NetworkObserver(false, "", MainActivity()).createNewNetworkRequest())

        /*Thread().run {
            NetworkObserver(false, "", MainActivity()).createNewNetworkRequest()
        }*/

        /*
        Observable.defer {
            try {
                Observable.just(AppConstants.RESPONSE_DATA)
            } catch (e: Exception) {
                Observable.error<Exception>(e)
            }
        }.subscribeOn(Schedulers.io())
                .subscribe(Subscriber<String>)
                .subscribe(
                    { onNext -> println("${AppConstants.RESPONSE_DATA} \nNew Resp Logged")},
                    { onError -> println(onError.toString() + " onError area")},
                    { }
                )*/


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
        /*println("New dataset ${AppConstants.RESPONSE_DATA.string()}")

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, BaseCountryDataSet::class.java)
        val jsonAdapter: JsonAdapter<List<BaseCountryDataSet>> = moshi.adapter(type)

        try {
            AppConstants.WORLD_DATA = jsonAdapter.fromJson(AppConstants.RESPONSE_DATA.string())!!
            println("Data set is valid")
        } catch (e: Exception) {
            println("Data set is invalid")
            e.printStackTrace()
        }*/
        println(AppConstants.WORLD_DATA[0].country)
    }

    private fun updateData() {
        // Moshi adapter code
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, BaseCountryDataSet::class.java)
        val jsonAdapter: JsonAdapter<List<BaseCountryDataSet>> = moshi.adapter(type)

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
            val currentLocation: LocationDataSet = AppUtils().getLocationData(this)
            Log.d("LocationInfo", currentLocation.toString())
        } catch (e: Exception) {
            Log.d("LocationInfo", "no location found")
            e.printStackTrace()
        }
    }
}
