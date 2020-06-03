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

    override fun onCreate() {
        super.onCreate()

        Log.d("CovidTesting", "Timer delay is ${AppConstants.TIMER_DELAY} minutes")
        timer.schedule(object: TimerTask() {
            override fun run() {
                Log.d("CovidTesting","Running service . . .")
                Thread(Runnable {
                    Looper.prepare()
                    NetworkObserver(3, null, null, AppConstants.APP_OPEN).createNewNetworkRequest()
                }).start()
            }
        }, 0, 2*60*1000)
    }

    override fun stopService(name: Intent?): Boolean {
        Log.d("CovidTesting","Stopping service . . .")
        return super.stopService(name)
    }
}