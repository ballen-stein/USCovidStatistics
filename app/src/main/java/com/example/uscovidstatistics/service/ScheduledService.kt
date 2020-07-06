package com.example.uscovidstatistics.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationManagerCompat
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.NotificationDataset
import com.example.uscovidstatistics.model.apidata.*
import com.example.uscovidstatistics.network.NetworkRequests
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.utils.PreferenceUtils
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
import kotlin.collections.ArrayList

class ScheduledService : Service() {

    private val timer = Timer()

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private lateinit var response: Response

    private lateinit var prefUtils: PreferenceUtils

    private lateinit var appUtils: AppUtils

    private lateinit var notificationCountriesList: ArrayList<NotificationDataset>

    private var caseIndex = 0

    private var endOfList = false

    private var notificationWasShown = false

    private var checkValue: Long = 30L

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        if (AppConstants.Notification_Service_On) {
            prefUtils = PreferenceUtils.getInstance(this)
            prefUtils.userPreferences()
            appUtils = AppUtils.getInstance()

            checkValue = AppConstants.User_Prefs.getLong(getString(R.string.preference_notification_frequency), 30L)

            AppConstants.Data_Specifics = 0
            Observable.defer {
                try {
                    val networkRequests = NetworkRequests(0, null, null).getLocationData()
                    Observable.just(networkRequests)
                } catch (e: Exception) {
                    Observable.error<Exception>(e)
                }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    { onNext -> response = onNext as Response
                        setData(response) },
                    { onError ->  serviceErrors(onError) },
                    { startUpdateCheck() }
                )
        }
    }

    private fun startUpdateCheck() {
        AppConstants.Data_Specifics = 7

        notificationCountriesList  = AppUtils.getInstance().startNotificationService(this)
        notificationCountriesList.toMutableList()

        if (notificationCountriesList.isNotEmpty()) {
            for ((i,country) in notificationCountriesList.withIndex()) {
                timer.schedule(object: TimerTask() {
                    override fun run() {
                        Thread(Runnable {
                            Looper.prepare()
                            Observable.defer {
                                try {
                                    val networkRequests = NetworkRequests(7, null, country.name).getLocationData()
                                    Observable.just(networkRequests)
                                } catch (e: Exception) {
                                    Observable.error<Exception>(e)
                                }
                            }.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe (
                                    { onNext -> response = onNext as Response
                                        setData(response) },
                                    { onError ->  serviceErrors(onError) },
                                    { notificationsCheck(country.name, i)
                                        if (i == notificationCountriesList.size-1)
                                            endOfList = true
                                    }
                                )
                        }).start()
                    }
                }, 5000, checkValue*60*1000)
            }
        }
    }

    private fun serviceErrors(onError: Throwable?) {
        if (onError is Error) {
            this.stopSelf()
            Handler().postDelayed({
                startService(Intent(this, ScheduledService::class.java))
            }, 1*60*1000)
        }
    }

    private fun setData(response: Response) {
        val body = response.body!!
        val type: ParameterizedType = Types.newParameterizedType(
            when (AppConstants.Data_Specifics) {
                0,1,3 -> List::class.java
                else -> List::class.java
            },
            when (AppConstants.Data_Specifics) {
                0 -> BaseCountryDataset::class.java
                1,2 -> StateDataset::class.java
                3 -> ContinentDataset::class.java
                else -> BaseCountryDataset::class.java
            })

        try {
            when (AppConstants.Data_Specifics) {
                0 -> {
                    val jsonAdapter: JsonAdapter<List<BaseCountryDataset>> = moshi.adapter(type)
                    AppConstants.World_Data = jsonAdapter.fromJson(body.string())!!

                    for (data in AppConstants.World_Data) {
                        AppConstants.World_Data_Mapped[data.country!!] = data
                    }
                }
                1 -> {
                    val jsonAdapter: JsonAdapter<ArrayList<StateDataset>> = moshi.adapter(type)
                    AppConstants.Us_Data = jsonAdapter.fromJson(body.string())!!
                    for (data in AppConstants.Us_Data) {
                        AppConstants.Us_State_Data_Mapped[data.state!!] = data
                    }
                }
                2 -> { // Type isn't used since JSON data is not a list
                    val jsonAdapter = moshi.adapter(StateDataset::class.java)
                    AppConstants.Us_State_Data = jsonAdapter.fromJson(body.string())!!
                }
                3 -> {
                    val jsonAdapter: JsonAdapter<List<ContinentDataset>> = moshi.adapter(type)
                    AppConstants.Continent_Data = jsonAdapter.fromJson(body.string())!!
                }
                4 -> { // Type isn't used since JSON data is not a list
                    val jsonAdapter = moshi.adapter(JhuCountryDataset::class.java)
                    AppConstants.Country_Data = jsonAdapter.fromJson(body.string())!!
                }
                5 -> { // Type isn't used since JSON data is not a list
                    val jsonAdapter = moshi.adapter(JhuProvinceDataset::class.java)
                    AppConstants.Country_Province_Data = jsonAdapter.fromJson(body.string())!!
                }
                7 -> {
                    val jsonAdapter: JsonAdapter<BaseCountryDataset> = moshi.adapter(BaseCountryDataset::class.java)
                    val updatedBaseData = jsonAdapter.fromJson(body.string())
                    AppConstants.World_Data_Mapped[updatedBaseData!!.country!!] = updatedBaseData
                }
                else -> {
                    val jsonAdapter: JsonAdapter<List<BaseCountryDataset>> = moshi.adapter(type)
                    AppConstants.World_Data = jsonAdapter.fromJson(body.string())!!
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        body.close()
    }

    private fun notificationsCheck(country: String, i: Int) {
        val notificationData = AppConstants.World_Data_Mapped[country]!!
        var compiledData = ""

        if (AppConstants.User_Prefs.getBoolean(getString(R.string.preference_notifications), false)) {
            if (notificationCountriesList[i].casesValue <= notificationData.cases!! && AppConstants.User_Prefs.getBoolean(getString(R.string.preference_notif_cases), false)) {
                compiledData = "Cases: ${appUtils.formatNumbers(notificationCountriesList[i].casesValue)} (${appUtils.formatNumbers(notificationData.cases!!)} total)."
                notificationCountriesList[i].casesMetricMet = true
                notificationWasShown = true
                notifications(caseIndex, compiledData, country)
                caseIndex++
            }

            if (notificationCountriesList[i].casesValue <= notificationData.cases!! && AppConstants.User_Prefs.getBoolean(getString(R.string.preference_notif_recovered), false)) {
                compiledData = "Recoveries: ${appUtils.formatNumbers(notificationCountriesList[i].recoverValue)} (${appUtils.formatNumbers(notificationData.recovered!!)} total)."
                notificationCountriesList[i].recoverMetricMet = true
                notificationWasShown = true
                notifications(caseIndex, compiledData, country)
                caseIndex++
            }

            if (notificationCountriesList[i].casesValue <= notificationData.cases!! && AppConstants.User_Prefs.getBoolean(getString(R.string.preference_notif_deaths), false)) {
                compiledData = "Deaths: ${appUtils.formatNumbers(notificationCountriesList[i].deathValue)} (${appUtils.formatNumbers(notificationData.deaths!!)} total)."
                notificationCountriesList[i].deathMetricMet = true
                notificationWasShown = true
                notifications(caseIndex, compiledData, country)
                caseIndex++
            }
        }

        if (endOfList) {
            caseIndex = 0
            endOfList = false

            if (notificationWasShown)
                appUtils.updateNotificationNumber(this, notificationCountriesList)
        }
    }

    private fun notifications(index: Int, compiledData: String, country: String) {
        with(NotificationManagerCompat.from(applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            notify(index, appUtils.newNotification(applicationContext, compiledData, country)!!.build())
        }
    }
}