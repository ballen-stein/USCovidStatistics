package com.example.uscovidstatistics.views.activities.usersettings

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.databinding.ActivitySettingsBinding
import com.example.uscovidstatistics.views.dialogs.BottomDialog
import com.example.uscovidstatistics.views.navigation.BaseActivity
import kotlinx.android.synthetic.main.app_toolbar.view.*

class UserSettings : BaseActivity(), ViewBinding {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(root)

        setSupportActionBar(root.bottom_toolbar)
        setNavOptions()
    }

    private fun setNavOptions() {
        binding.regionBackBtn.setOnClickListener {
            onBackPressed()
        }

        root.bottom_toolbar.setNavigationOnClickListener {
            BottomDialog(this).newInstance().show(supportFragmentManager, "BottomDialog")
        }
    }

    override fun getRoot(): View {
        return binding.root
    }
}
