package com.example.uscovidstatistics.service

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.util.Log
import com.example.uscovidstatistics.network.NetworkObserver

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        //val activity = context as Activity
        Log.d("CovidTesting","Running data . . .")
        Thread(Runnable {
            Looper.prepare()
            NetworkObserver(context!!, 3, null, null, false).createNewNetworkRequest()
        }).start()

    }

}