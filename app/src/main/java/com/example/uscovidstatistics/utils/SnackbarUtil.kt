package com.example.uscovidstatistics.utils

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.example.uscovidstatistics.R
import com.google.android.material.snackbar.Snackbar

class SnackbarUtil (private val mContext: Context) {

    fun error() {

    }

    fun info(view: View, text: String) {
        return Snackbar.make(view,
            text,
            Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(mContext, R.color.colorTertiary))
            .setTextColor(ContextCompat.getColor(mContext, R.color.colorInfoText)).show()
    }

    fun retry() {

    }
}