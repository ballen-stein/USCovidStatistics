package com.example.uscovidstatistics.views.activities.homepage

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.ActivityMainBinding
import com.example.uscovidstatistics.manualdependency.DependencyInjectorImpl
import com.example.uscovidstatistics.model.CleanedUpData
import com.example.uscovidstatistics.service.ScheduledService
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.views.activities.country.CountryActivity
import com.example.uscovidstatistics.views.dialogs.BottomDialog
import com.example.uscovidstatistics.views.navigation.BaseActivity
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.app_toolbar.view.*
import java.lang.Exception

class MainActivity : BaseActivity(), ViewBinding, MainContract.View {
    private lateinit var binding: ActivityMainBinding

    private lateinit var presenter: MainContract.Presenter

    private val appUtils = AppUtils.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //gpsCords = intent.getDoubleArrayExtra(AppConstants.CURRENT_GPS_LOCATION)
        AppConstants.APP_OPEN = true
        AppConstants.DATA_SPECIFICS = 3
        AppConstants.TIMER_DELAY = AppUtils().setTimerDelay()

        setPresenter(MainPresenter(this, DependencyInjectorImpl()))
        presenter.onViewCreated()

        /*
        (Thread(Runnable {
            //NetworkRequests(this, 0, null, null).createNewNetworkRequest()
            //NetworkRequests(this, 1, null, null).createNewNetworkRequest()
            //NetworkRequests(this, 2, null, "California").createNewNetworkRequest()
            NetworkRequests(this, 3, null, null, true).createNewNetworkRequest()
            //NetworkRequests(this, 4, "Canada", null).createNewNetworkRequest()
            //NetworkRequests(this, 5, "Canada", "ontario").createNewNetworkRequest()
        }).start()
        */

        setSupportActionBar(binding.root.bottom_toolbar)
        setNavListener()
    }

    private fun setNavListener() {
        Log.d("CovidTesting", "Navigation is set in MAIN")
        binding.root.bottom_toolbar.setNavigationOnClickListener {
            Toasty.info(this, "Navigation is pressed", Toast.LENGTH_LONG).show()
            BottomDialog().newInstance().show(supportFragmentManager, "BottomDialog")
        }
    }

    fun onGpsClick(view: View) {
        Log.d("CovidTesting", "Showing GPS Information . . .$view")
        if (appUtils.gpsPermissionGranted(this)) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    AppConstants.GPS_DATA[0] = location.longitude
                    AppConstants.GPS_DATA[1] = location.latitude
                }

                val intent = Intent(this, CountryActivity::class.java)
                //intent.putExtra(AppConstants.DISPLAY_GPS, 0)
                intent.putExtra(AppConstants.DISPLAY_COUNTRY, "Canada")
                startActivity(intent)
            }
        } else {
            appUtils.checkLaunchPermissions(this)
        }
    }

    override fun onStop() {
        super.onStop()
        AppConstants.APP_OPEN = false
        val intent = Intent(this, ScheduledService::class.java)
        if (!AppConstants.APP_OPEN) {
            Log.d("CovidTesting", "Stopping service . . .")
            stopService(intent)
        }
        Log.d("CovidTesting", "Starting service . . .")
        //this.startService(intent)
    }

    override fun onStart() {
        super.onStart()
        Log.d("CovidTesting", "Starting service with ${AppConstants.TIMER_DELAY/60000} minute delay")
        Handler().postDelayed(
            {presenter.onServiceStarted(this)}, AppConstants.TIMER_DELAY
        )
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
            binding.globalCases.text = AppUtils().formatNumbers(continentData[0])
        } else if (binding.globalCases.text != AppUtils().formatNumbers(continentData[0])) {
            Log.d("CovidTesting","Formatting data . . .")
            binding.globalCases.text = AppUtils().formatNumbers(continentData[0])
        }
        binding.globalRecovered.text = AppUtils().formatNumbers(continentData[1])
        binding.globalDeaths.text = AppUtils().formatNumbers(continentData[2])

        binding.currentInfected.text = AppUtils().formatNumbers(continentData[3])

        val mildText = AppUtils().formatNumbers(continentData[4]) + " (${AppUtils().getStringPercent(continentData[4], continentData[3])}%)"
        binding.currentMild.text = mildText

        val criticalText = AppUtils().formatNumbers(continentData[5]) + " (${AppUtils().getStringPercent(continentData[5], continentData[3])}%)"
        binding.currentCritical.text = criticalText

        binding.currentClosed.text = AppUtils().formatNumbers(continentData[6])
        binding.currentDischarged.text = binding.globalRecovered.text
        binding.currentDead.text = binding.globalDeaths.text
    }

    override fun dataError() {
        Toasty.info(this, R.string.no_connection, Toast.LENGTH_LONG).show()
    }
}
