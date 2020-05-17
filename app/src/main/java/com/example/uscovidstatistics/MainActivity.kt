package com.example.uscovidstatistics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.uscovidstatistics.utils.LaunchUtil
import es.dmoral.toasty.Toasty

class MainActivity : AppCompatActivity() {
    private lateinit var utilities : LaunchUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        utilities = LaunchUtil(context = this, activity = MainActivity())
    }


    override fun onResume() {
        super.onResume()

        if (utilities.checkPermissions()) {
            Toasty.error(this, "Local GPS permission not granted", Toast.LENGTH_SHORT).show()
        } else {
            Toasty.info(this, "GPS Permission granted", Toast.LENGTH_SHORT).show()
        }

    }
}
