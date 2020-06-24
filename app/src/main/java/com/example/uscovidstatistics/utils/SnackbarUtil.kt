package com.example.uscovidstatistics.utils

import android.content.Context
import android.view.View
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
        return Snackbar.make(view.rootView, text, Snackbar.LENGTH_INDEFINITE)
            .setBackgroundTint(ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
            .setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
            .setAnchorView(view)
            .show()
    }
}