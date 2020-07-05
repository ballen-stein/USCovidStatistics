package com.example.uscovidstatistics.views.activities.splash

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.views.activities.homepage.MainActivity
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.ActivitySplashBinding
import com.example.uscovidstatistics.views.activities.BaseActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception
import java.net.UnknownHostException
import kotlin.collections.HashMap

class Splash : BaseActivity(), ViewBinding, SplashContract.View {

    private lateinit var loadGpsIntent: Intent

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var presenter: SplashContract.Presenter

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(root)

        loadGpsIntent = Intent(this, MainActivity::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setPresenter(SplashPresenter(this))
        presenter.onViewCreated(this)
        presenter.getCountryFlags()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionMap = HashMap<String, Int>()
        for ((i, perm) in permissions.withIndex())
            permissionMap[perm] = grantResults[i]

        if (requestCode == AppConstants.Request_Gps_Location && permissionMap[Manifest.permission.ACCESS_COARSE_LOCATION] != 0) {
            launchApp()
        } else {
            presenter.requestGps()
        }
    }

    override fun setPresenter(presenter: SplashContract.Presenter) {
        this.presenter = presenter
    }

    override fun launchApp() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onError(throwable: Throwable) {
        Log.d("CovidTesting", "Error with $throwable")

        // Setup a restart function to try and reattempt a network request
        if (throwable is Exception) {
            Log.d("CovidTesting", "$throwable inside Main is an Exception")
        } else if (throwable is Error) {
            Log.d("CovidTesting", "$throwable inside Main is an Error")
        }
        if (throwable is RuntimeException) {
            Log.d("CovidTesting", "$throwable inside Main is a Runtime Exception")
        }

        // Snackbars for Throwables when I can determine how each Throwable is caused
        if (throwable is UnknownHostException) {
            // Enables wifi if there's no connection
            Snackbar.make(root, R.string.snackbar_error_wifi, Snackbar.LENGTH_INDEFINITE)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.colorRed))
                .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                .setAction(R.string.snackbar_clk_enable){
                    presenter.networkStatus()
                }.setActionTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                .show()
        } else {
            // Restarts the API due to timeout/other errors out of the user's control
            Snackbar.make(root, R.string.snackbar_error_timeout, Snackbar.LENGTH_INDEFINITE)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.colorRed))
                .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                .setAction(R.string.snackbar_clk_retry){
                    presenter.getCountryFlags()
                }.setActionTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                .show()
        }
    }

    override fun getRoot(): View {
        return binding.root
    }
}
