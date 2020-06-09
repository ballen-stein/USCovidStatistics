package com.example.uscovidstatistics.views.activities.country

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.ActivityCountryBreakdownBinding
import com.example.uscovidstatistics.manualdependency.DependencyInjectorImpl
import com.example.uscovidstatistics.model.CleanedUpData
import com.example.uscovidstatistics.model.apidata.JhuCountryDataset
import com.example.uscovidstatistics.recyclerview.CleanedDataRecyclerView
import com.example.uscovidstatistics.utils.AppUtils
import kotlinx.android.synthetic.main.app_toolbar.view.*
import okhttp3.internal.notify
import okhttp3.internal.notifyAll

class CountryActivity : AppCompatActivity(), ViewBinding, CountryContract.View {
    private lateinit var binding: ActivityCountryBreakdownBinding

    private lateinit var presenter: CountryContract.Presenter

    private val appUtils = AppUtils.getInstance()

    private lateinit var countryDisplay: String

    private val cleanedDataList = ArrayList<CleanedUpData>()

    private lateinit var recyclerViewData: CleanedDataRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryBreakdownBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        countryDisplay = intent.getStringExtra(AppConstants.DISPLAY_COUNTRY)!!

        AppConstants.COUNTRY_NAME = countryDisplay
        AppConstants.DATA_SPECIFICS = 4
        //AppConstants.TIMER_DELAY = appUtils.setTimerDelay()

        setHeader()
        setPresenter(CountryPresenter(this, DependencyInjectorImpl()))
        presenter.onViewCreated()

        recyclerViewData = CleanedDataRecyclerView(this, this)
    }

    private fun setHeader() {
        val url = AppConstants.WORLD_DATA_MAPPED[countryDisplay]!!.countryInfo!!.countryFlag

        Glide.with(this)
            .load(url)
            .into(binding.flag1)

        Glide.with(this)
            .load(url)
            .into(binding.flag2)

        val headerText = "$countryDisplay Information"
        binding.casesHeader.text = headerText
    }

    fun getCleanedUpData(): List<CleanedUpData> {
        cleanedDataList.sortBy { it.name }
        cleanedDataList.add(appUtils.cleanCountryTotals())
        return cleanedDataList
    }

    override fun setPresenter(presenter: CountryContract.Presenter) {
        this.presenter = presenter
    }

    override fun getRoot(): View {
        TODO("Not yet implemented")
    }

    override fun displayCountryData(countryData: JhuCountryDataset) {
        val regionList = countryData.province!!
        presenter.getRegionalData(regionList)
    }

    override fun displayCountryList() {
        for (data in AppConstants.COUNTRY_PROVINCE_LIST)
            cleanedDataList.add(appUtils.cleanCountryData(data))

        recyclerViewData.displayCleanedData()
    }

    override fun onStop() {
        super.onStop()
        AppConstants.COUNTRY_PROVINCE_LIST.clear()
        appUtils.resetCountryTotals()
    }

    override fun dataError() {
        Log.d("CovidTesting", "Error with country list! ")
    }
}
