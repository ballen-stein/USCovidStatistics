package com.example.uscovidstatistics.views.activities.splash

import android.content.Context
import android.location.Location
import android.os.Handler
import android.util.Log
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.apidata.BaseCountryDataset
import com.example.uscovidstatistics.model.apidata.JhuBaseDataset
import com.example.uscovidstatistics.network.NetworkRequests
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.utils.PreferenceUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import java.net.UnknownHostException
import java.util.*

class SplashPresenter (view: SplashContract.View) : SplashContract.Presenter {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private var view: SplashContract.View? = view

    private lateinit var prefUtil: PreferenceUtils

    private val appUtils = AppUtils.getInstance()

    private lateinit var mContext: Context

    private val timer = Timer()

    override fun onDestroy() {
        this.view = null
    }

    override fun onViewCreated(mContext: Context) {
        this.mContext = mContext
        prefUtil = PreferenceUtils.getInstance(mContext)
        prefUtil.baseInit()
    }

    override fun getCountryFlags() {
        if (appUtils.checkNetwork(mContext)) {
            Observable.defer {
                try {
                    val networkRequests = NetworkRequests(0, null, null).getLocationData()
                    Observable.just(networkRequests)
                } catch (e: Exception) {
                    Observable.error<Exception>(e)
                }
            }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {onNext -> onNext as Response
                        setWorldData(onNext)
                    },
                    { onError -> view?.onError(onError) },
                    { setLocationalData() }
                )
        } else {
            //throw UnknownHostException("Wifi/Data is Off") {
            view?.onError(UnknownHostException())
        }
    }

    private fun setWorldData(response: Response) {
        val body = response.body!!
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type: ParameterizedType = Types.newParameterizedType(List::class.java, BaseCountryDataset::class.java)
        val jsonAdapter: JsonAdapter<List<BaseCountryDataset>> = moshi.adapter(type)
        AppConstants.World_Data = jsonAdapter.fromJson(body.string())!!

        for (data in AppConstants.World_Data) {
            AppConstants.World_Data_Mapped[data.country!!] = data
        }
    }

    private fun setLocationalData() {
        Observable.defer {
            try {
                val networkRequests = NetworkRequests(6, null, null).getLocationData()
                Observable.just(networkRequests)
            } catch (e: Exception) {
                Observable.error<Exception>(e)
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {onNext -> onNext as Response
                    saveLocationData(onNext)
                },
                { onError -> view?.onError(onError) },
                { requestGps() }
            )
    }

    private fun saveLocationData(response: Response) {
        val body = response.body!!
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type: ParameterizedType = Types.newParameterizedType(List::class.java, JhuBaseDataset::class.java)
        val jsonAdapter: JsonAdapter<List<JhuBaseDataset>> = moshi.adapter(type)
        AppConstants.Regional_Data = jsonAdapter.fromJson(body.string())!!
    }

    override fun requestGps() {
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext)
        if (AppUtils().checkLaunchPermissions(mContext)) {
            setGpsCoords(fusedLocationClient)
        }
    }

    private fun setGpsCoords(fusedLocationClient: FusedLocationProviderClient) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? -> if (location != null) {
            AppConstants.Gps_Data[0] = location.longitude
            AppConstants.Gps_Data[1] = location.latitude
        }
            setLocation()
        }
    }

    private fun setLocation() {
        try {
            AppConstants.Location_Data = AppUtils().getLocationData(mContext)
        } catch (e: Exception) {
            view?.onError(e)
            e.printStackTrace()
        }
        view?.launchApp()
    }

    override fun networkStatus() {
        appUtils.restoreNetwork(mContext)
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (AppConstants.Wifi_Check && appUtils.checkNetwork(mContext)) {
                    this.cancel()
                    getCountryFlags()
                    AppConstants.Wifi_Check = false
                }
            }
        }, 0, 500)
    }
}