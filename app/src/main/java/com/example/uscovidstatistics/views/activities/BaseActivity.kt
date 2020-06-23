package com.example.uscovidstatistics.views.activities

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.utils.PreferenceUtils
import com.example.uscovidstatistics.utils.SnackbarUtil
import com.example.uscovidstatistics.views.activities.country.CountryActivity
import com.example.uscovidstatistics.views.activities.usersettings.UserSettings
import com.example.uscovidstatistics.views.dialogs.BottomDialog
import com.example.uscovidstatistics.views.dialogs.SearchDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import es.dmoral.toasty.Toasty
import java.lang.Exception

open class BaseActivity : AppCompatActivity() {

    private lateinit var navigationBar: Toolbar

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
            R.id.app_bar_settings -> {
                val intent = Intent(this, UserSettings::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.enter_right, R.anim.exit_left)
                //Toasty.info(this, "Settings", Toast.LENGTH_SHORT).show()
            }
            R.id.app_bar_contact -> {
                Toasty.info(this, "Contact Us", Toast.LENGTH_SHORT).show()
            }
            R.id.app_bar_favorite -> {
                PreferenceUtils.getInstance(this).addToSavedList(AppConstants.COUNTRY_NAME!!)
                if (!PreferenceUtils(this).checkPref(AppConstants.COUNTRY_NAME!!)) {
                    item.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_24px)
                    SnackbarUtil(this).info(navigationBar.rootView, getString(R.string.snackbar_favorite_removed))
                    Toasty.info(this, "Removed", Toast.LENGTH_SHORT).show()
                } else {
                    item.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_24px)
                    SnackbarUtil(this).info(navigationBar.rootView, getString(R.string.snackbar_favorite_added))
                    Toasty.info(this, "Added", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_left, R.anim.exit_right)
        AppConstants.PREFERENCE_CHECK = true
        finish()
    }


    @Override
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionMap = HashMap<String, Int>()
        Toasty.info(this, "GPS Permission granted", Toast.LENGTH_SHORT).show()
        for ((i, perm) in permissions.withIndex())
            permissionMap[perm] = grantResults[i]

        if (requestCode == AppConstants.REQUEST_GPS_LOCATION && permissionMap[Manifest.permission.ACCESS_COARSE_LOCATION] == 0) {
            setGpsCoords()
        }
    }


    private fun setGpsCoords() {
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                Log.d("CovidTesting", "Setting GPS data")
                AppConstants.GPS_DATA[0] = location.longitude
                AppConstants.GPS_DATA[1] = location.latitude
                AppConstants.LOCATION_DATA = AppUtils().getLocationData(this)
                goToGpsLocation()
            }
        }
    }

    private fun goToGpsLocation() {
        try {
            if (AppConstants.LOCATION_DATA.country != null) {
                val country = AppConstants.LOCATION_DATA.country
                val region = AppConstants.LOCATION_DATA.region
                val intent = Intent(this, CountryActivity::class.java)
                    .putExtra(AppConstants.DISPLAY_REGION, region)

                if (country == "United States") {
                    intent.putExtra(AppConstants.DISPLAY_COUNTRY, "USA")
                        .putExtra(AppConstants.LOAD_STATE, true)
                } else
                    intent.putExtra(AppConstants.DISPLAY_COUNTRY, country)

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