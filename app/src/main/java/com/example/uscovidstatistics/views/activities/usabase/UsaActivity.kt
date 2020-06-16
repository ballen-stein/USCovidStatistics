package com.example.uscovidstatistics.views.activities.usabase

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.ActivityUsaBinding
import com.example.uscovidstatistics.views.navigation.BaseActivity

class UsaActivity : BaseActivity(), ViewBinding {

    private lateinit var binding: ActivityUsaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsaBinding.inflate(layoutInflater)
        setContentView(root)

        AppConstants.DATA_SPECIFICS = 1
    }

    override fun getRoot(): View {
        return binding.root
    }
}
