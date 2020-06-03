package com.example.uscovidstatistics.views.activities.homepage

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.DataResponseListener
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.ActivityMainBinding
import com.example.uscovidstatistics.manualdependency.DependencyInjectorImpl
import com.example.uscovidstatistics.network.NetworkObserver
import com.example.uscovidstatistics.service.ScheduledService
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.utils.MathUtils
import okhttp3.Response

class MainActivity : AppCompatActivity(), ViewBinding, MainContract.View {
    private val TAG = "MainActivityTag"
    private lateinit var response: Response
    private var gpsCords: DoubleArray? = DoubleArray(2)

    private lateinit var binding: ActivityMainBinding

    private lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //gpsCords = intent.getDoubleArrayExtra(AppConstants.CURRENT_GPS_LOCATION)
        //AppConstants.dataResponseListener = this
        AppConstants.APP_OPEN = true
        AppConstants.DATA_SPECIFICS = 3
        AppConstants.TIMER_DELAY = AppUtils().setTimerDelay()

        setPresenter(MainPresenter(this, DependencyInjectorImpl()))
        presenter.onViewCreated()

        /*
        (Thread(Runnable {
            //NetworkObserver(this, 0, null, null).createNewNetworkRequest()
            //NetworkObserver(this, 1, null, null).createNewNetworkRequest()
            //NetworkObserver(this, 2, null, "California").createNewNetworkRequest()
            NetworkObserver(this, 3, null, null, true).createNewNetworkRequest()
            //NetworkObserver(this, 4, "Canada", null).createNewNetworkRequest()
            //NetworkObserver(this, 5, "Canada", "ontario").createNewNetworkRequest()
        }).start()

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

        Handler().postDelayed(
            {startBackgroundTask(this)}, AppConstants.TIMER_DELAY
        )
        */

    }

    fun onGpsClick(view: View) {
        Log.d("CovidTesting", "Showing GPS Information . . .")
        /*binding.root.fab.setOnClickListener {

        }*/
    }

    override fun onStop() {
        super.onStop()
        AppConstants.APP_OPEN = false
        //startBackgroundTask(this)
    }

    override fun onResume() {
        super.onResume()
        //AppConstants.dataResponseListener = this
    }

    private fun startBackgroundTask(context: Context) {
        val intent = Intent(context, ScheduledService::class.java)
        if (!AppConstants.APP_OPEN) {
            Log.d("CovidTesting", "Stopping service . . .")
            stopService(intent)
        }
        Log.d("CovidTesting", "Starting service . . .")
        context.startService(intent)
    }

    override fun getRoot(): View {
        TODO("Not yet implemented")
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    override fun displayContinentData(continentData: IntArray) {
        Log.d("CovidTesting", "data is now : ${continentData.contentToString()}")

        if (binding.globalCases.text == null) {
            binding.globalCases.text = MathUtils().formatNumbers(continentData[0])
        } else if (binding.globalCases.text != MathUtils().formatNumbers(continentData[0])) {
            Log.d("CovidTesting","Formatting data . . .")
            binding.globalCases.text = MathUtils().formatNumbers(continentData[0])
        }
        binding.globalRecovered.text = MathUtils().formatNumbers(continentData[1])
        binding.globalDeaths.text = MathUtils().formatNumbers(continentData[2])

        binding.currentInfected.text = MathUtils().formatNumbers(continentData[3])
        val mildText = MathUtils().formatNumbers(continentData[4]) + " (${MathUtils().getStringPercent(continentData[4], continentData[3])}%)"
        binding.currentMild.text = mildText
        val criticalText = MathUtils().formatNumbers(continentData[5]) + " (${MathUtils().getStringPercent(continentData[5], continentData[3])}%)"
        binding.currentCritical.text = criticalText

        binding.currentClosed.text = MathUtils().formatNumbers(continentData[6])
        binding.currentDischarged.text = binding.globalRecovered.text
        binding.currentDead.text = binding.globalDeaths.text

    }

    /*override fun uiUpdateData(success: Boolean) {
        Log.d("CovidTesting","Getting data . . .")
        if (success) {
            val uiData = AppUtils().getCurrentData(0) as IntArray

            println(uiData.contentToString())

            if (binding.globalCases.text == null) {
                binding.globalCases.text = MathUtils().formatNumbers(uiData[0])
            } else if (binding.globalCases.text != MathUtils().formatNumbers(uiData[0])) {
                Log.d("CovidTesting","Formatting data . . .")
                binding.globalCases.text = MathUtils().formatNumbers(uiData[0])
            }
            binding.globalRecovered.text = MathUtils().formatNumbers(uiData[1])
            binding.globalDeaths.text = MathUtils().formatNumbers(uiData[2])

            binding.currentInfected.text = MathUtils().formatNumbers(uiData[3])
            val mildText = MathUtils().formatNumbers(uiData[4]) + " (${MathUtils().getStringPercent(uiData[4], uiData[3])}%)"
            binding.currentMild.text = mildText
            val criticalText = MathUtils().formatNumbers(uiData[5]) + " (${MathUtils().getStringPercent(uiData[5], uiData[3])}%)"
            binding.currentCritical.text = criticalText

            binding.currentClosed.text = MathUtils().formatNumbers(uiData[6])
            binding.currentDischarged.text = binding.globalRecovered.text
            binding.currentDead.text = binding.globalDeaths.text
        } else {
            print("")
        }
    }*/
}
