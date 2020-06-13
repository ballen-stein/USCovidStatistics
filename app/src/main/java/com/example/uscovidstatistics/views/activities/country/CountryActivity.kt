package com.example.uscovidstatistics.views.activities.country

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.ActivityCountryBreakdownBinding
import com.example.uscovidstatistics.manualdependency.DependencyInjectorImpl
import com.example.uscovidstatistics.model.CleanedUpData
import com.example.uscovidstatistics.model.apidata.JhuCountryDataset
import com.example.uscovidstatistics.recyclerview.CleanedDataRecyclerView
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.views.dialogs.BottomDialog
import com.example.uscovidstatistics.views.navigation.BaseActivity
import kotlinx.android.synthetic.main.app_toolbar.view.*
import java.lang.Exception

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
        val view = binding.root
        setContentView(view)

        countryDisplay = intent.getStringExtra(AppConstants.DISPLAY_COUNTRY)!!

        AppConstants.COUNTRY_NAME = countryDisplay
        AppConstants.DATA_SPECIFICS = 4

        setHeader()
        setNavOptions()
        setSupportActionBar(binding.root.bottom_toolbar)

        setPresenter(CountryPresenter(this, DependencyInjectorImpl()))
        presenter.onViewCreated()

        recyclerViewData = CleanedDataRecyclerView(this, this)
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
        val regionList = if (countryData.province!!.contains("bonaire, sint eustatius and saba")) {
            val temp = countryData.province!!.toMutableList()
            temp.remove("bonaire, sint eustatius and saba")
            temp.toTypedArray()
        } else {
            countryData.province!!
        }
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
