package com.example.uscovidstatistics.views.activities.homepage

import android.content.Context
import android.content.Intent
import android.os.Looper
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.manualdependency.DependencyInjector
import com.example.uscovidstatistics.model.DataModelRepository
import com.example.uscovidstatistics.model.apidata.*
import com.example.uscovidstatistics.network.NetworkRequests
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.views.activities.country.CountryActivity
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
import javax.inject.Inject

class MainPresenter @Inject constructor(view: MainContract.View, dependencyInjector: DependencyInjector) : MainContract.Presenter {

    private val dataModelRepository: DataModelRepository = dependencyInjector.covidDataRepository()

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private var view: MainContract.View? = view

    override fun onDestroy() {
        this.view = null
    }

    override fun onViewCreated() {
        loadData(AppConstants.Data_Specifics)
    }

    override fun openLocationOnLaunch(mContext: Context) {
        if (AppConstants.User_Prefs.getBoolean(mContext.getString(R.string.preference_gps), false) && AppUtils.getInstance().gpsPermissionGranted(mContext)) {
            if (AppConstants.Location_Data.country != null) {
                val country = AppConstants.Location_Data.country
                val region = AppConstants.Location_Data.region
                val intent = Intent(mContext, CountryActivity::class.java)
                    .putExtra(AppConstants.Display_Region, region)

                if (country == "United States") {
                    intent.putExtra(AppConstants.Display_Country, "USA")
                        .putExtra(AppConstants.Load_State, true)
                } else
                    intent.putExtra(AppConstants.Display_Country, country)

                mContext.startActivity(intent)

            }
        }
    }

    override fun updateData() {
        for ((i,country) in AppConstants.Saved_Locations.withIndex()) {
            if (country == "Global") continue
            Observable.defer {
                try {
                    val networkRequest = NetworkRequests(7, null, country).getLocationData()
                    Observable.just(networkRequest)
                } catch (e: Exception) {
                    Observable.error<Exception>(e)
                }
            }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {onNext -> onNext as Response
                        updateListData(onNext)},
                    {onError -> view?.dataError(onError)},
                    {
                        if (i == AppConstants.Saved_Locations.size-1) {
                            view?.displayContinentData(AppUtils().continentTotals(dataModelRepository.getContinentData()))
                        }
                    }
                )
        }
    }

    private fun updateListData(response: Response) {
        val body = response.body!!
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<BaseCountryDataset> = moshi.adapter(BaseCountryDataset::class.java)

        try {
            val updatedBaseData = jsonAdapter.fromJson(body.string())
            AppConstants.World_Data_Mapped[updatedBaseData!!.country!!] = updatedBaseData
        } catch (e: Exception) {
            view?.dataError(e)
            e.printStackTrace()
        }
        body.close()
    }

    override fun onServiceStarted() {
        val timer = Timer()
        timer.schedule(object: TimerTask() {
            override fun run() {
                Thread(Runnable {
                    Looper.prepare()
                    loadData(3)
                    loadData(0)
                    if (AppConstants.Saved_Locations.size >= 2) updateData()
                }).start()
            }
        },0, (AppConstants.Update_Frequency)*60*1000)
    }

    private fun loadData(getSpecifics: Int) {
        Observable.defer {
            try {
                val networkRequests = NetworkRequests(getSpecifics, null, null).getLocationData()
                Observable.just(networkRequests)
            } catch (e: Exception) {
                Observable.error<Exception>(e)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { onNext -> onNext as Response
                    setData(onNext, getSpecifics) },
                { onError ->  view?.dataError(onError) },
                { view?.displayContinentData(AppUtils().continentTotals(dataModelRepository.getContinentData())) }
            )
    }

    private fun setData(response: Response, specifics: Int) {
        val body = response.body!!
        try {
            when (specifics) {
                0 -> {
                    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                    val type: ParameterizedType = Types.newParameterizedType(List::class.java, BaseCountryDataset::class.java)
                    val jsonAdapter: JsonAdapter<List<BaseCountryDataset>> = moshi.adapter(type)
                    AppConstants.World_Data = jsonAdapter.fromJson(body.string())!!

                    for (data in AppConstants.World_Data) {
                        AppConstants.World_Data_Mapped[data.country!!] = data
                    }
                }
                3 -> {
                    val type: ParameterizedType = Types.newParameterizedType(List::class.java, ContinentDataset::class.java)

                    val jsonAdapter: JsonAdapter<List<ContinentDataset>> = moshi.adapter(type)
                    AppConstants.Continent_Data = jsonAdapter.fromJson(body.string())!!
                }
            }
        } catch (e: Exception) {
            view?.dataError(e)
            e.printStackTrace()
        }
        body.close()
    }
}