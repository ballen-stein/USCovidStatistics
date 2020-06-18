package com.example.uscovidstatistics.views.activities.region

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.ActivityRegionBinding
import com.example.uscovidstatistics.model.apidata.JhuProvinceDataset
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.views.dialogs.BottomDialog
import com.example.uscovidstatistics.views.navigation.BaseActivity
import kotlinx.android.synthetic.main.app_toolbar.view.*

class RegionActivity : BaseActivity(), ViewBinding, RegionContract.View {

    private lateinit var binding: ActivityRegionBinding

    private lateinit var regionName: String

    private val appUtils = AppUtils.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val countryName = intent.getStringExtra(AppConstants.DISPLAY_COUNTRY)!!

        val namePreFormat = intent.getStringExtra(AppConstants.DISPLAY_REGION)!!
        regionName = appUtils.formatName(namePreFormat)

        binding.regionHeader.text = regionName

        if (countryName == "USA" || countryName == "United States of America") {
            Log.d("CovidTesting", "Url is : ${AppConstants.API_DATA_URL_USA}/$regionName/${AppConstants.API_DATA_ENDPOINT}")
        } else {
            Log.d("CovidTesting", "Url is : ${AppConstants.API_DATA_JHU_COUNTRY}/$countryName/$regionName")
        }

        setSupportActionBar(binding.root.bottom_toolbar)
        setNavOptions()
    }

    private fun setNavOptions() {
        binding.regionBackBtn.setOnClickListener {
            onBackPressed()
        }

        binding.root.bottom_toolbar.setNavigationOnClickListener {
            BottomDialog(this).newInstance().show(supportFragmentManager, "BottomDialog")
        }
    }

    override fun setPresenter(presenter: RegionContract.Presenter) {
        TODO("Not yet implemented")
    }


    override fun displayRegionData(regionDataset: JhuProvinceDataset) {
        TODO("Not yet implemented")
    }

    override fun dataError() {
        TODO("Not yet implemented")
    }

    override fun getRoot(): View {
        return binding.root
    }
}
