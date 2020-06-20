package com.example.uscovidstatistics.views.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.views.dialogs.BottomDialog
import com.example.uscovidstatistics.views.dialogs.SearchDialog
import es.dmoral.toasty.Toasty

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
        menuInflater.inflate(R.menu.bottom_appbar_menu, menu)
        setNavigationListeners()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_search -> {
                SearchDialog(this).newInstance().show(supportFragmentManager, "SearchDialog")
            }
            R.id.app_bar_settings -> {
                Toasty.info(this, "Settings", Toast.LENGTH_SHORT).show()
            }
            R.id.app_bar_contact -> {
                Toasty.info(this, "Contact Us", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_left, R.anim.exit_right)
        finish()
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