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
import com.example.uscovidstatistics.views.dialogs.BottomDialog
import com.example.uscovidstatistics.views.activities.BaseActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.app_toolbar.view.*
import kotlinx.android.synthetic.main.loading_screen.view.*

class MainActivity : BaseActivity(), ViewBinding, MainContract.View {
    private lateinit var binding: ActivityMainBinding

    private lateinit var presenter: MainContract.Presenter

    private lateinit var recyclerView: LocationsRecyclerView

    private val appUtils = AppUtils.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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
    }

    override fun onStart() {
        super.onStart()
        AppConstants.TIMER_DELAY = AppUtils().setTimerDelay()
        if (!AppConstants.GLOBAL_SERVICE_ON) {
            Handler().postDelayed(
                {presenter.onServiceStarted(this)}, AppConstants.TIMER_DELAY
            )
            AppConstants.GLOBAL_SERVICE_ON = true
        } else {
            Log.d("CovidTesting", "Service is already running")
        }
    }

    override fun getRoot(): View {
        return binding.root
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    override fun displayContinentData(continentData: IntArray) {
        if (binding.globalCases.text == "") {
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
        binding.currentDead.text = deathText

        cleanDataForCountries()
    }

    private fun cleanDataForCountries() {
        for (continent in AppConstants.CONTINENT_DATA) {
            val continentName = continent.continent
            val temp = appUtils.removeTerritories(continentName!!, binding.root.context)
            continent.countriesOnContinent = appUtils.cleanHashMap(appUtils.continentCountryList()[continentName]!!, temp)
        }
        binding.root.loading_layout.visibility = View.GONE
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
        //TODO Add functionality to access saved countries
    }
}
