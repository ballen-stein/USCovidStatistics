package com.example.uscovidstatistics.views.activities.state

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.ActivityRegionBinding
import com.example.uscovidstatistics.manualdependency.DependencyInjectorImpl
import com.example.uscovidstatistics.model.apidata.StateDataset
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.utils.SnackbarUtil
import com.example.uscovidstatistics.views.dialogs.BottomDialog
import com.example.uscovidstatistics.views.activities.BaseActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.app_toolbar.view.*
import java.net.UnknownHostException

class StateActivity : BaseActivity(), ViewBinding, StateContract.View {

    private lateinit var binding: ActivityRegionBinding

    private lateinit var presenter: StateContract.Presenter

    private lateinit var regionName: String

    private val appUtils = AppUtils.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val namePreFormat = intent.getStringExtra(AppConstants.Display_Region)!!
        regionName = appUtils.formatName(namePreFormat)
        binding.regionHeader.text = regionName

        AppConstants.Country_Name = intent.getStringExtra(AppConstants.Display_Country)!!
        AppConstants.Region_Name = regionName
        AppConstants.Data_Specifics = 2

        setPresenter(StatePresenter(this, DependencyInjectorImpl()))
        presenter.onViewCreated(this)

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
        binding.informationView.visibility = View.VISIBLE
        binding.dataProgress.visibility = View.GONE
        setStateData(AppConstants.Us_State_Data)
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
        // SnackBar to turn on Wifi/Data if both are off, otherwise SnackBar to attempt another API call due to timeout, incomplete data, etc.
        if (throwable is UnknownHostException) {
            // Enables wifi if there's no connection
            Snackbar.make(root, R.string.snackbar_error_wifi, Snackbar.LENGTH_INDEFINITE)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.colorRed))
                .setAnchorView(root.bottom_toolbar)
                .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                .setAction(R.string.snackbar_clk_enable){
                    presenter.networkStatus(this)
                }.setActionTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                .show()
        } else {
            // Restarts the API due to timeout/other errors out of the user's control
            Snackbar.make(root, R.string.snackbar_error_timeout, Snackbar.LENGTH_INDEFINITE)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.colorRed))
                .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                .setAnchorView(root.bottom_toolbar)
                .setAction(R.string.snackbar_clk_retry){
                    presenter.onViewCreated(this)
                }.setActionTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                .show()
        }
    }

    override fun onResumeData() {
        runOnUiThread {
            SnackbarUtil(this).info(root.bottom_toolbar, getString(R.string.snackbar_wifi_enabled))
            presenter.onViewCreated(this)
        }
    }

    override fun getRoot(): View {
        return binding.root
    }
}
