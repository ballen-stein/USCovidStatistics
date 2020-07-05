package com.example.uscovidstatistics.views.activities

import android.Manifest
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.utils.PreferenceUtils
import com.example.uscovidstatistics.utils.SnackbarUtil
import com.example.uscovidstatistics.views.activities.country.CountryActivity
import com.example.uscovidstatistics.views.activities.splash.Splash
import com.example.uscovidstatistics.views.activities.usersettings.UserSettings
import com.example.uscovidstatistics.views.dialogs.BottomDialog
import com.example.uscovidstatistics.views.dialogs.SearchDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomappbar.BottomAppBar
import es.dmoral.toasty.Toasty
import java.lang.Exception

open class BaseActivity : AppCompatActivity() {

    private lateinit var navigationBar: BottomAppBar

    private var activityVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_toolbar)
        navigationBar = findViewById(R.id.bottom_toolbar)
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        setSupportActionBar(navigationBar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottom_appbar_main_menu, menu)
        setNavigationListeners()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_search -> {
                SearchDialog(this).newInstance().show(supportFragmentManager, "SearchDialog")
            }
            R.id.app_bar_contact -> {
                val msgIntent = Intent(Intent.ACTION_SENDTO).apply {
                    type = "text/plain"
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, "ballenstein58@gmail.com")
                    putExtra(Intent.EXTRA_SUBJECT, "Question about the Covid Statistic App")
                }
                
                if (msgIntent.resolveActivity(packageManager) != null)
                    startActivity(msgIntent)
            }
            R.id.app_bar_favorite -> {
                val activityView = findViewById<BottomAppBar>(R.id.bottom_toolbar)
                PreferenceUtils.getInstance(this).addToSavedList(AppConstants.Country_Name!!)

                if (!PreferenceUtils(this).checkPref(AppConstants.Country_Name!!)) {
                    item.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_24px)
                    SnackbarUtil(this).info(activityView, getString(R.string.snackbar_favorite_removed))
                } else {
                    item.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_24px)
                    SnackbarUtil(this).info(activityView, getString(R.string.snackbar_favorite_added))
                }
            }
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_left, R.anim.exit_right)
        AppConstants.Preference_Check = true
        finish()
    }

    @Override
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionMap = HashMap<String, Int>()
        for ((i, perm) in permissions.withIndex())
            permissionMap[perm] = grantResults[i]

        if (requestCode == AppConstants.Request_Gps_Location && permissionMap[Manifest.permission.ACCESS_COARSE_LOCATION] == 0) {
            setGpsCoords()
        } else if (requestCode == AppConstants.Request_Wifi_Change_Perm) {
            // TODO Add update to Wifi/Data enabling -- Data may not work sub Android 10 due to OS changes
        }
    }


    private fun setGpsCoords() {
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                AppConstants.Gps_Data[0] = location.longitude
                AppConstants.Gps_Data[1] = location.latitude
                AppConstants.Location_Data = AppUtils().getLocationData(this)

                if (this !is UserSettings)
                    goToGpsLocation()
                else {
                    PreferenceUtils.getInstance(this).prefSaveGps(true)
                    AppConstants.Settings_Updated = true
                }
            }
        }
    }

    private fun goToGpsLocation() {
        try {
            if (AppConstants.Location_Data.country != null) {
                val country = AppConstants.Location_Data.country
                val region = AppConstants.Location_Data.region
                val intent = Intent(this, CountryActivity::class.java)
                    .putExtra(AppConstants.Display_Region, region)

                if (country == "United States") {
                    intent.putExtra(AppConstants.Display_Country, "USA")
                        .putExtra(AppConstants.Load_State, true)
                } else
                    intent.putExtra(AppConstants.Display_Country, country)

                startActivity(intent)
                overridePendingTransition(R.anim.enter_right, R.anim.exit_left)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setNavigationListeners() {
        navigationBar.setNavigationOnClickListener {
            BottomDialog(this).newInstance().show(supportFragmentManager, "NavigationDialog")
        }
    }

    fun isActivityVisible(): Boolean {
        return activityVisible
    }

    open fun activityResumed() {
        activityVisible = true
    }

    open fun activityPaused() {
        activityVisible = false
    }
}