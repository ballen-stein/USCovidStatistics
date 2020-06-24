package com.example.uscovidstatistics.views.activities.homepage

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.ActivityMainBinding
import com.example.uscovidstatistics.manualdependency.DependencyInjectorImpl
import com.example.uscovidstatistics.model.apidata.BaseCountryDataset
import com.example.uscovidstatistics.recyclerview.LocationsRecyclerView
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.utils.PreferenceUtils
import com.example.uscovidstatistics.views.dialogs.BottomDialog
import com.example.uscovidstatistics.views.activities.BaseActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.app_toolbar.view.*

class MainActivity : BaseActivity(), ViewBinding, MainContract.View {
    private lateinit var binding: ActivityMainBinding

    private lateinit var presenter: MainContract.Presenter

    private lateinit var recyclerView: LocationsRecyclerView

    private val appUtils = AppUtils.getInstance()

    private lateinit var appPrefs: PreferenceUtils

    private var openOnLaunch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        appPrefs = PreferenceUtils.getInstance(this)
        appPrefs.userPreferences()


        if (AppConstants.USER_PREFS.getBoolean(getString(R.string.preference_gps), false))
            openOnLaunch = true

        AppConstants.APP_OPEN = true
        AppConstants.DATA_SPECIFICS = 3

        setPresenter(MainPresenter(this, DependencyInjectorImpl()))
        presenter.onViewCreated()
        recyclerView = LocationsRecyclerView(this, this)

        setSupportActionBar(binding.root.bottom_toolbar)
        setNavListener()
    }

    private fun setNavListener() {
        binding.root.bottom_toolbar.setNavigationOnClickListener {
            BottomDialog(this).newInstance().show(supportFragmentManager, "BottomDialog")
        }
    }

    override fun onStop() {
        super.onStop()
        this.activityPaused()
        AppConstants.APP_OPEN = false
    }

    override fun onResume() {
        super.onResume()
        this.activityResumed()
        AppConstants.APP_OPEN = true

        appPrefs.userPreferences()
        if (AppConstants.USER_PREFS.getInt(getString(R.string.preference_frequency), 0) != 0) {
            AppConstants.UPDATE_FREQUENCY = AppConstants.USER_PREFS.getInt(getString(R.string.preference_frequency), 5)
            Log.d("CovidTesting", "${AppConstants.UPDATE_FREQUENCY} has a set value!")
        } else {
            Log.d("CovidTesting", "No setting set!")
        }

        if (AppConstants.USER_PREFS.getString(getString(R.string.preference_saved_location), "") != null) {
            val savedLocations = AppConstants.USER_PREFS.getString(getString(R.string.preference_saved_location), "")!!.split("/")

            AppConstants.SAVED_LOCATIONS.clear()
            AppConstants.SAVED_LOCATIONS.addAll(savedLocations)

            if (AppConstants.SAVED_LOCATIONS.isNotEmpty() && AppConstants.PREFERENCE_CHECK) {
                recyclerView.displaySavedLocations()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        AppConstants.TIMER_DELAY = AppUtils().setTimerDelay()
        if (!AppConstants.GLOBAL_SERVICE_ON) {
            Handler().postDelayed(
                {presenter.onServiceStarted()}, AppConstants.TIMER_DELAY
            )
            AppConstants.GLOBAL_SERVICE_ON = true
        }
    }

    override fun getRoot(): View {
        return binding.root
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    private var globalData: BaseCountryDataset? = null

    override fun displayContinentData(continentData: BaseCountryDataset) {
        /*if (binding.globalCases.text == "") {
            binding.globalCases.text = appUtils.formatNumbers(continentData[0])
        } else if (binding.globalCases.text != appUtils.formatNumbers(continentData[0])) {
            Snackbar.make(root, "Global Data Updated", Snackbar.LENGTH_LONG)
                .setAnchorView(root.bottom_toolbar)
                .show()
            binding.globalCases.text = appUtils.formatNumbers(continentData[0])
        }

        binding.globalRecovered.text = appUtils.formatNumbers(continentData[1])
        binding.globalDeaths.text = appUtils.formatNumbers(continentData[2])

        val activeText = "${appUtils.formatNumbers(continentData[3])} (${appUtils.getStringPercent(continentData[3], continentData[0])}%)"
        binding.currentInfected.text = activeText

        val mildText = appUtils.formatNumbers(continentData[4]) + " (${appUtils.getStringPercent(continentData[4], continentData[3])}%)"
        binding.currentMild.text = mildText

        val criticalText = appUtils.formatNumbers(continentData[5]) + " (${appUtils.getStringPercent(continentData[5], continentData[3])}%)"
        binding.currentCritical.text = criticalText

        val closedText = "${appUtils.formatNumbers(continentData[6])} (${appUtils.getStringPercent(continentData[6], continentData[0])}%)"
        binding.currentClosed.text = closedText

        val recoveredText = "${appUtils.formatNumbers(continentData[1])} (${appUtils.getStringPercent(continentData[1], continentData[6])}%)"
        binding.currentDischarged.text = recoveredText

        val deathText = "${appUtils.formatNumbers(continentData[2])} (${appUtils.getStringPercent(continentData[2], continentData[6])}%)"
        binding.currentDead.text = deathText*/


        binding.dataProgress.visibility = View.VISIBLE

        if (AppConstants.UPDATING_DATA) {
            Snackbar.make(root, "Global Data Updated", Snackbar.LENGTH_LONG)
                .setAnchorView(root.bottom_toolbar)
                .show()
        } else {
            AppConstants.UPDATING_DATA = true
        }

        globalData = continentData
        cleanDataForCountries()
    }

    private fun cleanDataForCountries() {
        for (continent in AppConstants.CONTINENT_DATA) {
            val continentName = continent.continent
            val temp = appUtils.removeTerritories(continentName!!, binding.root.context)
            continent.countriesOnContinent = appUtils.cleanHashMap(appUtils.continentCountryList()[continentName]!!, temp)
        }

        if (openOnLaunch) {
            openOnLaunch = false
            presenter.openLocationOnLaunch(this)
        }

        binding.dataProgress.visibility = View.GONE
        recyclerView.displaySavedLocations()
    }

    override fun dataError(throwable: Throwable) {
        Log.d("CovidTesting", "Error with $throwable")
        /*

        Snackbar.make(root, "${R.string.no_connection}\nPlease try again in a few minutes", Snackbar.LENGTH_LONG)
            .setAnchorView(root.bottom_toolbar)
            .show()

         */
        // Setup a restart function to try and reattempt a network request
        if (throwable == Exception()) {
            Log.d("CovidTesting", "$throwable inside Main is an Exception")
        } else if (throwable == Error()) {
            Log.d("CovidTesting", "$throwable inside Main is an Error")
        }
        if (throwable == RuntimeException()) {
            Log.d("CovidTesting", "$throwable inside Main is a Runtime Exception")
        }

        Snackbar.make(root, R.string.snackbar_timeout, Snackbar.LENGTH_INDEFINITE)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorRed))
            .setAnchorView(root.bottom_toolbar)
            .setAction(R.string.snackbar_clk_retry){
                presenter.onViewCreated()
            }.setActionTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            .show()
    }


    fun getSavedLocations(): List<BaseCountryDataset> {
        val list = ArrayList<BaseCountryDataset>()
        for (countryName in  AppConstants.SAVED_LOCATIONS) {
            if (countryName == "Global")
                list.add(globalData!!)
            else {
                Log.d("CovidTesting", "Country in base : $countryName")
                list.add(AppConstants.WORLD_DATA_MAPPED[countryName]!!)
            }
        }
        return list
    }
}
