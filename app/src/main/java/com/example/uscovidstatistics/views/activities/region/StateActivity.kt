package com.example.uscovidstatistics.views.activities.region

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.ActivityRegionBinding
import com.example.uscovidstatistics.manualdependency.DependencyInjectorImpl
import com.example.uscovidstatistics.model.apidata.StateDataset
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.views.dialogs.BottomDialog
import com.example.uscovidstatistics.views.activities.BaseActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.app_toolbar.view.*
import kotlinx.android.synthetic.main.loading_screen.view.*

class StateActivity : BaseActivity(), ViewBinding, StateContract.View {

    private lateinit var binding: ActivityRegionBinding

    private lateinit var presenter: StateContract.Presenter

    private lateinit var regionName: String

    private val appUtils = AppUtils.getInstance()

    private lateinit var spinnerData: List<StateDataset>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val namePreFormat = intent.getStringExtra(AppConstants.DISPLAY_REGION)!!
        regionName = appUtils.formatName(namePreFormat)
        binding.regionHeader.text = regionName

        AppConstants.COUNTRY_NAME = intent.getStringExtra(AppConstants.DISPLAY_COUNTRY)!!
        AppConstants.REGION_NAME = regionName
        AppConstants.DATA_SPECIFICS = 2

        setPresenter(StatePresenter(this, DependencyInjectorImpl()))
        presenter.onViewCreated()
        spinnerData = presenter.getStateData()

        setSupportActionBar(binding.root.bottom_toolbar)
        setNavOptions()
    }

    private fun setNavOptions() {
        binding.viewBackBtn.setOnClickListener {
            onBackPressed()
        }

        root.bottom_toolbar.setNavigationOnClickListener {
            BottomDialog(this).newInstance().show(supportFragmentManager, "BottomDialog")
        }
    }

    override fun setPresenter(presenter: StateContract.Presenter) {
        this.presenter = presenter
    }

    override fun displayStateData(stateDataset: StateDataset) {
        root.loading_layout.visibility = View.GONE
        setStateData(AppConstants.US_STATE_DATA)
    }

    private fun setStateData(stateDataset: StateDataset) {
        val currentDate = appUtils.getFormattedDate()

        val tests = "${appUtils.formatNumbers(stateDataset.tests!!)} as of $currentDate"
        binding.stateTests.text = tests

        val cases = "${appUtils.formatNumbers(stateDataset.cases!!)} as of $currentDate"
        binding.stateCases.text = cases
        val todayCases = "${appUtils.formatNumbers(stateDataset.todayCases!!)} as of $currentDate"
        binding.stateTodayCases.text = todayCases

        val recovered = "${appUtils.formatNumbers(stateDataset.cases!!.minus(stateDataset.activeCases!!).minus(stateDataset.deaths!!))} as of $currentDate"
        binding.stateRecovered.text = recovered

        val deaths = "${appUtils.formatNumbers(stateDataset.deaths!!)} as of $currentDate"
        binding.stateDeaths.text = deaths
        val todayDeaths = "${appUtils.formatNumbers(stateDataset.todayDeaths!!)} as of $currentDate"
        binding.stateTodayDeaths.text = todayDeaths

        // Per One Million
        val casesPOM = "${stateDataset.perMillionCases} cases ${resources.getString(R.string.details_PerOneMil)}"
        binding.casesPom.text = casesPOM

        val testsPOM = "${stateDataset.perMillionTests} tests ${resources.getString(R.string.details_PerOneMil)}"
        binding.testsPom.text = testsPOM

        val deathPOM = "${stateDataset.perMillionDeaths} deaths ${resources.getString(R.string.details_PerOneMil)}"
        binding.deathsPom.text = deathPOM
    }

    override fun dataError(throwable: Throwable) {
        Log.d("CovidTesting", throwable.toString())

        if (throwable == Exception()) {
            Log.d("CovidTesting", "$throwable inside State is an Exception")
        } else if (throwable == Error()) {
            Log.d("CovidTesting", "$throwable inside State is an Error")
        }
        if (throwable == RuntimeException()) {
            Log.d("CovidTesting", "$throwable inside State is a Runtime Exception")
        }

        Snackbar.make(root, R.string.snackbar_timeout, Snackbar.LENGTH_INDEFINITE)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorRed))
            .setAnchorView(root.bottom_toolbar)
            .setAction(R.string.snackbar_retry){
                presenter.onViewCreated()
            }.setActionTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            .show()
    }

    override fun getRoot(): View {
        return binding.root
    }
}
