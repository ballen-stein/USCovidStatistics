package com.example.uscovidstatistics.views.activities.homepage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.ActivityMainBinding
import com.example.uscovidstatistics.manualdependency.DependencyInjectorImpl
import com.example.uscovidstatistics.service.ScheduledService
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.views.dialogs.BottomDialog
import com.example.uscovidstatistics.views.navigation.BaseActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.app_toolbar.view.*
import kotlinx.android.synthetic.main.loading_screen.view.*

class MainActivity : BaseActivity(), ViewBinding, MainContract.View {
    private lateinit var binding: ActivityMainBinding

    private lateinit var presenter: MainContract.Presenter

    private val appUtils = AppUtils.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        AppConstants.APP_OPEN = true
        AppConstants.DATA_SPECIFICS = 3
        AppConstants.TIMER_DELAY = AppUtils().setTimerDelay()

        setPresenter(MainPresenter(this, DependencyInjectorImpl()))
        presenter.onViewCreated()

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
        if (binding.globalCases.text == null) {
            binding.globalCases.text = appUtils.formatNumbers(continentData[0])
        } else if (binding.globalCases.text != appUtils.formatNumbers(continentData[0])) {
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
    }

    override fun dataError() {
        Toasty.info(this, R.string.no_connection, Toast.LENGTH_LONG).show()
        //TODO Add Snackbar
    }
}
