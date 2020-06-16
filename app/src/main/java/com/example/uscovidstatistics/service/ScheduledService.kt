package com.example.uscovidstatistics.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.apidata.*
import com.example.uscovidstatistics.network.NetworkRequests
import com.example.uscovidstatistics.utils.AppUtils
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Response
import java.lang.Exception
import java.lang.reflect.ParameterizedType
import java.util.*

class ScheduledService : Service() {

    private val timer = Timer()
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private lateinit var response: Response

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        timer.schedule(object: TimerTask() {
            override fun run() {
                Log.d("CovidTesting","Running service . . .")
                Thread(Runnable {
                    Looper.prepare()

                    Observable.defer {
                        try {
                            val networkRequests = NetworkRequests(AppConstants.DATA_SPECIFICS, null, null).getLocationData()
                            Observable.just(networkRequests)
                        } catch (e: Exception) {
                            Observable.error<Exception>(e)
                        }
                    }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe (
                            { onNext -> response = onNext as Response
                                setData(response) },
                            { onError ->  Log.d("CovidTesting", "Error in the subscription : $onError")},
                            { notifications()
                                //response.body!!.close()
                            }
                        )
                }).start()
            }
        }, 5000, 3*60*1000)
    }

    private fun setData(response: Response) {
        val body = response.body!!
        val type: ParameterizedType = Types.newParameterizedType(
            when (AppConstants.DATA_SPECIFICS) {
                0,1,3 -> List::class.java
                else -> List::class.java
            },
            when (AppConstants.DATA_SPECIFICS) {
                0 -> BaseCountryDataset::class.java
                1,2 -> StateDataset::class.java
                3 -> ContinentDataset::class.java
                else -> BaseCountryDataset::class.java
            })

        try {
            when (AppConstants.DATA_SPECIFICS) {
                0 -> {
                    val jsonAdapter: JsonAdapter<List<BaseCountryDataset>> = moshi.adapter(type)
                    AppConstants.WORLD_DATA = jsonAdapter.fromJson(body.string())!!
                }
                1 -> {
                    val jsonAdapter: JsonAdapter<ArrayList<StateDataset>> = moshi.adapter(type)
                    AppConstants.US_DATA = jsonAdapter.fromJson(body.string())!!
                    for (data in AppConstants.US_DATA) {
                        AppConstants.US_STATE_DATA_MAPPED[data.state!!] = data
                    }
                }
                2 -> { // Type isn't used since JSON data is not a list
                    val jsonAdapter = moshi.adapter(StateDataset::class.java)
                    AppConstants.US_STATE_DATA = jsonAdapter.fromJson(body.string())!!
                }
                3 -> {
                    val jsonAdapter: JsonAdapter<List<ContinentDataset>> = moshi.adapter(type)
                    AppConstants.CONTINENT_DATA = jsonAdapter.fromJson(body.string())!!                }
                4 -> { // Type isn't used since JSON data is not a list
                    val jsonAdapter = moshi.adapter(JhuCountryDataset::class.java)
                    AppConstants.COUNTRY_DATA = jsonAdapter.fromJson(body.string())!!
                }
                5 -> { // Type isn't used since JSON data is not a list
                    val jsonAdapter = moshi.adapter(JhuProvinceDataset::class.java)
                    AppConstants.COUNTRY_PROVINCE_DATA = jsonAdapter.fromJson(body.string())!!
                }
                else -> {
                    val jsonAdapter: JsonAdapter<List<BaseCountryDataset>> = moshi.adapter(type)
                    AppConstants.WORLD_DATA = jsonAdapter.fromJson(body.string())!!
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        body.close()
    }

    private fun notifications() {
        println("Updated time  : ${AppConstants.CONTINENT_DATA[0].timeUpdated}")
        println("Current cases : ${AppConstants.CONTINENT_DATA[0].cases}")

        with(NotificationManagerCompat.from(applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, AppUtils().newNotification(applicationContext)!!.build())
        }
    }

    override fun stopService(name: Intent?): Boolean {
        Log.d("CovidTesting","Stopping service . . .")
        return super.stopService(name)
    }
}