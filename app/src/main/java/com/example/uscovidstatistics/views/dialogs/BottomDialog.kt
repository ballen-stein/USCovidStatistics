package com.example.uscovidstatistics.views.dialogs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.BottomNavDialogFragmentBinding
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.views.activities.country.CountryActivity
import com.example.uscovidstatistics.views.activities.homepage.MainActivity
import com.example.uscovidstatistics.views.activities.usersettings.UserSettings
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
        if (AppConstants.Location_Data.country != null) {
            val country = AppConstants.Location_Data.country
            val region = AppConstants.Location_Data.region
            val intent = Intent(mContext, CountryActivity::class.java)
                .putExtra(AppConstants.Display_Region, region)

            if (country == "United States") {
                intent.putExtra(AppConstants.Display_Country, "USA")
                    .putExtra(AppConstants.Load_State, true)
            } else
                intent.putExtra(AppConstants.Display_Country, country)

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