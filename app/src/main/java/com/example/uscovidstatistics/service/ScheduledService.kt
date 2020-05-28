package com.example.uscovidstatistics.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.network.NetworkObserver
import java.util.*

class ScheduledService : Service() {

    private val timer = Timer()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /*
    override fun onHandleIntent(p0: Intent?) {
        TODO("Not yet implemented")
    }*/

    override fun onCreate() {
        super.onCreate()
        /*timer.scheduleAtFixedRate(object: TimerTask() {
            override fun run() {
                Log.d("CovidTesting","Running data . . .")
                Thread(Runnable {
                    Looper.prepare()
                    NetworkObserver(applicationContext, 3, null, null).createNewNetworkRequest()
                }).start()
            }
        }, 60, 50*60*1000)

         */
        timer.schedule(object: TimerTask() {
            override fun run() {
                Log.d("CovidTesting","Running data . . .")
                Thread(Runnable {
                    Looper.prepare()
                    NetworkObserver(applicationContext, 3, null, null, AppConstants.APP_OPEN).createNewNetworkRequest()
                }).start()
            }
        }, 0, 2*60*1000)
    }

    override fun stopService(name: Intent?): Boolean {
        Log.d("CovidTesting","Stopping data . . .")
        return super.stopService(name)
    }
}