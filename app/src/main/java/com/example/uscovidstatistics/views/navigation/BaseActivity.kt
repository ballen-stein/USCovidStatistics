package com.example.uscovidstatistics.views.navigation

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.views.dialogs.BottomDialog
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.bottom_nav_dialog_fragment.*
import kotlinx.android.synthetic.main.navigation_country_selection.*

open class BaseActivity : AppCompatActivity() {

    private lateinit var navigationBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_toolbar)
        navigationBar = findViewById(R.id.bottom_toolbar)
        //setSupportActionBar(navigationBar)
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        setSupportActionBar(navigationBar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottom_appbar_menu, menu)
        setNavigationListeners()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_gps -> Toasty.info(this, "Gps", Toast.LENGTH_SHORT).show()
            R.id.app_bar_home -> Toasty.info(this, "Home", Toast.LENGTH_SHORT).show()
            R.id.app_bar_settings -> Toasty.info(this, "Settings", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    private val navigationFragment: BottomDialog? = BottomDialog().newInstance()

    private fun setNavigationListeners() {
        navigationBar.setNavigationOnClickListener {
            navigationFragment!!.show(supportFragmentManager, "NavigationDialog")
        }
    }
}