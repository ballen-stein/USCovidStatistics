package com.example.uscovidstatistics.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.uscovidstatistics.R
import com.google.android.material.snackbar.Snackbar

class SnackbarUtil (private val mContext: Context) {

    fun error(view: View, text: String) {
        return Snackbar.make(view.rootView, text, Snackbar.LENGTH_INDEFINITE)
            .setBackgroundTint(ContextCompat.getColor(mContext, R.color.colorRed))
            .setAnchorView(view)
            .setActionTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
            .show()
    }

    fun info(view: View, text: String) {
        val snack = Snackbar.make(view.rootView, text, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
            .setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
            .setAnchorView(view)

        val snackView = snack.view
        snackView.findViewById<TextView>(R.id.snackbar_text).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
        return snack.show()
    }
}