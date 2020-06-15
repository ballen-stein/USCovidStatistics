package com.example.uscovidstatistics.views.navigation

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.views.activities.homepage.MainActivity
import com.example.uscovidstatistics.views.dialogs.BottomDialog
import es.dmoral.toasty.Toasty

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
            R.id.app_bar_settings -> {
                Toasty.info(this, "Settings", Toast.LENGTH_SHORT).show()
                //val intent = Intent(this, MainActivity::class.java)
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                //startActivity(intent)
                //overridePendingTransition(R.anim.enter_right, R.anim.exit_left)
            }
            R.id.app_bar_contact -> Toasty.info(this, "Contact Us", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_left, R.anim.exit_right)
    }

    private fun setNavigationListeners() {
        navigationBar.setNavigationOnClickListener {
            BottomDialog(this).newInstance().show(supportFragmentManager, "NavigationDialog")
        }
    }
}