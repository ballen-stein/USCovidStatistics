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
import com.example.uscovidstatistics.utils.SnackbarUtil
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

    private var updateList = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        appPrefs = PreferenceUtils.getInstance(this)
        appPrefs.userPreferences()


        if (AppConstants.USER_PREFS.getBoolean(getString(R.string.preference_gps), false))
            openOnLaunch = true

        AppConstants.App_Open = true
        AppConstants.Data_Specifics = 3

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
        AppConstants.App_Open = false
    }

    override fun onResume() {
        super.onResume()
        this.activityResumed()
        AppConstants.App_Open = true

        appPrefs.userPreferences()
        if (AppConstants.USER_PREFS.getLong(getString(R.string.preference_frequency), 0L) != 0L) {
            AppConstants.Update_Frequency = AppConstants.USER_PREFS.getLong(getString(R.string.preference_frequency), 5L)
        }

        if (AppConstants.USER_PREFS.getString(getString(R.string.preference_saved_location), "") != null) {
            val savedLocations = AppConstants.USER_PREFS.getString(getString(R.string.preference_saved_location), "")!!.split("/")

            AppConstants.Saved_Locations.clear()
            AppConstants.Saved_Locations.addAll(savedLocations)

            if (AppConstants.Saved_Locations.isNotEmpty() && AppConstants.Preference_Check) {
                recyclerView.displaySavedLocations()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        AppConstants.Timer_Delay = AppUtils().setTimerDelay()
        if (!AppConstants.Global_Service_On) {
            Handler().postDelayed(
                {presenter.onServiceStarted()}, AppConstants.Timer_Delay
            )
            AppConstants.Global_Service_On = true
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
        binding.dataProgress.visibility = View.VISIBLE

        if (AppConstants.Updating_Global && this.isActivityVisible()) {
            val dataUpdated = "Global ${getString(R.string.data_updated)}"
            SnackbarUtil(this).info(root.bottom_toolbar, dataUpdated)
        } else {
            AppConstants.Updating_Global = true
            updateList = true
        }

        globalData = continentData
        cleanDataForCountries()
    }

    private fun cleanDataForCountries() {
        for (continent in AppConstants.Continent_Data) {
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
        for (countryName in  AppConstants.Saved_Locations) {
            if (countryName == "Global") list.add(globalData!!) else list.add(AppConstants.World_Data_Mapped[countryName]!!)
        }
        return list
    }
}
