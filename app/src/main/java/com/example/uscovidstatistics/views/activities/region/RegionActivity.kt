package com.example.uscovidstatistics.views.activities.region

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.ActivityRegionBinding
import com.example.uscovidstatistics.model.apidata.JhuProvinceDataset
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.views.navigation.BaseActivity

class RegionActivity : BaseActivity(), ViewBinding, RegionContract.View {

    private lateinit var binding: ActivityRegionBinding

    private lateinit var regionName: String

    private val appUtils = AppUtils.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val namePreFormat = intent.getStringExtra(AppConstants.DISPLAY_REGION)!!
        regionName = appUtils.formatName(namePreFormat)

        binding.regionHeader.text = regionName

        binding.regionBackBtn.setOnClickListener {
            onBackPressed()
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
