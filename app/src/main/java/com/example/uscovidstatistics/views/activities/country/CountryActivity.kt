package com.example.uscovidstatistics.views.activities.country

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.ActivityCountryBreakdownBinding
import com.example.uscovidstatistics.manualdependency.DependencyInjectorImpl
import com.example.uscovidstatistics.model.CleanedUpData
import com.example.uscovidstatistics.model.apidata.BaseCountryDataset
import com.example.uscovidstatistics.model.apidata.JhuCountryDataset
import com.example.uscovidstatistics.recyclerview.CleanedDataRecyclerView
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.views.activities.homepage.MainActivity
import com.example.uscovidstatistics.views.dialogs.BottomDialog
import com.example.uscovidstatistics.views.activities.BaseActivity
import com.example.uscovidstatistics.views.activities.region.StateActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.app_toolbar.view.*
import kotlinx.android.synthetic.main.loading_screen.view.*
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.collections.ArrayList

class CountryActivity : BaseActivity(), ViewBinding, CountryContract.View {
    private lateinit var binding: ActivityCountryBreakdownBinding

    private lateinit var presenter: CountryContract.Presenter

    private val appUtils = AppUtils.getInstance()

    private lateinit var countryDisplay: String

    private val cleanedDataList = ArrayList<CleanedUpData>()

    private lateinit var recyclerViewData: CleanedDataRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryBreakdownBinding.inflate(layoutInflater)
        setContentView(root)

        // Testing
        AppConstants.COUNTRY_PROVINCE_LIST.clear()
        appUtils.resetCountryTotals()

        countryDisplay = intent.getStringExtra(AppConstants.DISPLAY_COUNTRY)!!

        AppConstants.USA_CHECK = (countryDisplay == "USA")
        AppConstants.COUNTRY_NAME = countryDisplay
        AppConstants.DATA_SPECIFICS = if (AppConstants.USA_CHECK) 1 else 4

        setPresenter(CountryPresenter(this, DependencyInjectorImpl(), this))
        presenter.onViewCreated(countryDisplay)

        recyclerViewData = CleanedDataRecyclerView(this, this)

        setSupportActionBar(binding.root.bottom_toolbar)
        setHeader()
        setNavOptions()
    }

    private fun setHeader() {
        try {
            val mappedName = if (countryDisplay == "Burma")
                "Myanmar"
            else
                countryDisplay
            val url = AppConstants.WORLD_DATA_MAPPED[mappedName]!!.countryInfo!!.countryFlag

            Glide.with(this)
                .load(url)
                .apply(RequestOptions().placeholder(R.drawable.ic_earth_flag))
                .into(binding.flag)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val headerText = "$countryDisplay Information"
        binding.casesHeader.text = headerText
    }

    private fun setNavOptions() {
        binding.viewBackBtn.setOnClickListener {
            onBackPressed()
        }

        binding.root.bottom_toolbar.setNavigationOnClickListener {
            BottomDialog(this).newInstance().show(supportFragmentManager, "BottomDialog")
        }
    }

    fun getCleanedUpData(): List<CleanedUpData> {
        if (AppConstants.USA_CHECK) {
            val tempData = ArrayList<CleanedUpData>()
            tempData.addAll(cleanedDataList)
            cleanedDataList.clear()
            cleanedDataList.addAll(appUtils.cleanUsaData(this, tempData))
        } else {
            cleanedDataList.sortBy { it.name }
        }
        cleanedDataList.add(appUtils.cleanCountryTotals())
        return cleanedDataList
    }

    override fun onStart() {
        super.onStart()
        AppConstants.TIMER_DELAY = AppUtils().setTimerDelay()
        if (!AppConstants.COUNTRY_SERVICE_ON) {
            Handler().postDelayed(
                {presenter.onServiceStarted(this)}, AppConstants.TIMER_DELAY
            )
            AppConstants.COUNTRY_SERVICE_ON = true
        } else {
            Log.d("CovidTesting", "Country Service is already running")
        }
    }

    override fun onResume() {
        super.onResume()
        this.activityResumed()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        overridePendingTransition(R.anim.enter_left, R.anim.exit_right)
    }

    override fun onStop() {
        super.onStop()
        this.activityPaused()
        AppConstants.COUNTRY_PROVINCE_LIST.clear()
        appUtils.resetCountryTotals()
    }

    override fun setPresenter(presenter: CountryContract.Presenter) {
        this.presenter = presenter
    }

    override fun getRoot(): View {
        return binding.root
    }

    override fun displayCountryData(countryData: JhuCountryDataset) {
        val regionList = if (countryData.province!!.contains("bonaire, sint eustatius and saba")) {
            val temp = countryData.province!!.toMutableList()
            temp.remove("bonaire, sint eustatius and saba")
            temp.toTypedArray()
        } else {
            countryData.province!!
        }

        displayGeneralData(AppConstants.WORLD_DATA_MAPPED[countryData.country!!]!!)

        AppConstants.DATA_SPECIFICS = 5
        presenter.getRegionalData(AppConstants.DATA_SPECIFICS, regionList)
    }

    private fun displayGeneralData(baseCountryDataset: BaseCountryDataset) {
        //val countryHeaderText = "${baseCountryDataset.country} ${resources.getString(R.string.base_covid_stats)}"
        //binding.countryLayoutHeader.text = countryHeaderText

        val statisticsHeader = "${resources.getString(R.string.base_covid_stats)} as of ${appUtils.getFormattedDate()}"
        binding.countryLayoutHeader.text = statisticsHeader

        val popFormatted = "${baseCountryDataset.population?.let { appUtils.formatNumbers(it) }} as of ${appUtils.getFormattedDate()}"
        binding.countryLayoutPopulation.text = popFormatted

        val casesFormatted = "${baseCountryDataset.cases?.let { appUtils.formatNumbers(it) }} (${appUtils.formatPopulation(baseCountryDataset, 0)})"
        binding.countryLayoutCases.text = casesFormatted

        val recoveredFormatted = "${baseCountryDataset.recovered?.let {appUtils.formatNumbers(it)}} (${appUtils.formatPopulation(baseCountryDataset, 1)})"
        binding.countryLayoutRecovered.text = recoveredFormatted

        val deathsFormatted = "${baseCountryDataset.deaths?.let { appUtils.formatNumbers(it) }} (${appUtils.formatPopulation(baseCountryDataset, 2)})"
        binding.countryLayoutDeaths.text = deathsFormatted

        // Per One Million
        val casesPOM = "${baseCountryDataset.perMillionCases} cases ${resources.getString(R.string.details_PerOneMil)}"
        binding.casesPom.text = casesPOM

        val testsPOM = "${baseCountryDataset.perMillionTests} tests ${resources.getString(R.string.details_PerOneMil)}"
        binding.testsPom.text = testsPOM

        val activePOM = "${baseCountryDataset.perMillionActive} active cases ${resources.getString(R.string.details_PerOneMil)}"
        binding.activePom.text = activePOM

        val recoverdPOM = "${baseCountryDataset.perMillionRecovered} recovered cases ${resources.getString(R.string.details_PerOneMil)}"
        binding.recoveredPom.text = recoverdPOM

        val criticalPOM = "${baseCountryDataset.perMillionCritical} critical cases ${resources.getString(R.string.details_PerOneMil)}"
        binding.criticalPom.text = criticalPOM

        val deathPOM = "${baseCountryDataset.perMillionDeaths} deaths ${resources.getString(R.string.details_PerOneMil)}"
        binding.deathsPom.text = deathPOM
    }

    override fun displayCountryList() {
        if (AppConstants.USA_CHECK) {
            for (state in AppConstants.US_DATA)
                cleanedDataList.add(appUtils.createCleanUsaData(state))
        } else {
            if (countryDisplay == "UK")
                countryDisplay = "United Kingdom"
            for (data in AppConstants.REGIONAL_DATA) {
                if (data.country!! == countryDisplay) {
                    if (data.province != null) {
                        cleanedDataList.add(appUtils.cleanRegionalData(data))
                    } else {
                        nullProvinceData()
                        break
                    }
                }
            }
        }

        recyclerViewData.displayCleanedData()
        binding.root.loading_layout.visibility = View.GONE


        if (intent.getBooleanExtra(AppConstants.LOAD_STATE, false)) {
            val intent = Intent(this, StateActivity::class.java)
            intent.putExtra(AppConstants.DISPLAY_COUNTRY, "USA")
                .putExtra(AppConstants.DISPLAY_REGION, this.intent.getStringExtra(AppConstants.DISPLAY_REGION))

            startActivity(intent)
            overridePendingTransition(R.anim.enter_right, R.anim.exit_left)
        }
    }

    private fun nullProvinceData() {
        for (province in AppConstants.COUNTRY_PROVINCE_LIST)
            cleanedDataList.add(appUtils.cleanCountryData(province))
    }

    override fun displayUsList() {
        for (data in AppConstants.US_DATA) {
            when {
                resources.getStringArray(R.array.us_territories).contains(data.state) -> {
                    data.state = "YYYY${data.state}"
                }
                resources.getStringArray(R.array.us_other).contains(data.state) -> {
                    data.state = "ZZZZ${data.state}"
                }
                else -> {
                    data.state = data.state
                }
            }
        }

        displayGeneralData(AppConstants.WORLD_DATA_MAPPED["USA"]!!)
        displayCountryList()
    }

    override fun dataError(throwable: Throwable) {
        Log.d("CovidTesting", "Error with country list! $throwable")
        if (throwable == Exception()) {
            Log.d("CovidTesting", "$throwable inside Country is an Exception")
        } else if (throwable == Error()) {
            Log.d("CovidTesting", "$throwable inside Country is an Error")
        }
        if (throwable == RuntimeException()) {
            Log.d("CovidTesting", "$throwable inside Country is a Runtime Exception")
        }

        Snackbar.make(root, R.string.snackbar_timeout, Snackbar.LENGTH_INDEFINITE)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorRed))
            .setAnchorView(root.bottom_toolbar)
            .setAction(R.string.snackbar_retry){
                presenter.onViewCreated(countryDisplay)
            }.setActionTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            .show()
    }
}
