package com.example.uscovidstatistics.views.dialogs

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.BottomNavDialogFragmentBinding
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.views.activities.BaseActivity
import com.example.uscovidstatistics.views.activities.country.CountryActivity
import com.example.uscovidstatistics.views.activities.homepage.MainActivity
import com.example.uscovidstatistics.views.activities.region.StateActivity
import com.example.uscovidstatistics.views.activities.usersettings.UserSettings
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.bottom_nav_dialog_fragment.*

class BottomDialog(private val mContext: Context) : BottomSheetDialogFragment(), ViewBinding{

    private lateinit var binding: BottomNavDialogFragmentBinding

    private val appUtils = AppUtils.getInstance()

    fun newInstance() : BottomDialog {
        return BottomDialog(mContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomNavDialogFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navigation_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    if (mContext !is MainActivity) {
                        val intent = Intent(mContext, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        (mContext as Activity).overridePendingTransition(R.anim.enter_left, R.anim.exit_right)
                    } else {
                        dismiss()
                    }
                }
                R.id.menu_notifications -> {
                    val intent = Intent(mContext, UserSettings::class.java)
                    startActivity(intent)
                    (mContext as Activity).overridePendingTransition(R.anim.enter_right, R.anim.exit_left)
                    dismiss()
                }
                R.id.menu_my_location -> {
                    if (appUtils.gpsPermissionGranted(mContext)) {
                        goToLocation()
                    } else {
                        // Will be heard with BaseActivity
                        appUtils.checkLaunchPermissions(mContext)
                    }
                    dismiss()
                }
                R.id.app_bar_settings -> {
                    val intent = Intent(mContext, UserSettings::class.java)
                    startActivity(intent)
                    (mContext as Activity).overridePendingTransition(R.anim.enter_right, R.anim.exit_left)
                    dismiss()
                }
            }
            true
        }
    }

    private fun goToLocation() {
        if (AppConstants.LOCATION_DATA.country != null) {
            val country = AppConstants.LOCATION_DATA.country
            val region = AppConstants.LOCATION_DATA.region
            val intent = Intent(mContext, CountryActivity::class.java)
                .putExtra(AppConstants.DISPLAY_REGION, region)

            if (country == "United States") {
                intent.putExtra(AppConstants.DISPLAY_COUNTRY, "USA")
                    .putExtra(AppConstants.LOAD_STATE, true)
            } else
                intent.putExtra(AppConstants.DISPLAY_COUNTRY, country)

            startActivity(intent)
            (mContext as Activity).overridePendingTransition(R.anim.enter_right, R.anim.exit_left)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.bottom_appbar_drawer_menu, menu)
    }

    override fun getRoot(): View {
        return binding.root
    }
}