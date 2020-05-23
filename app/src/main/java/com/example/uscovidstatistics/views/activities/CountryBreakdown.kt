package com.example.uscovidstatistics.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.databinding.ActivityCountryBreakdownBinding

class CountryBreakdown : AppCompatActivity(), ViewBinding {
    private lateinit var binding: ActivityCountryBreakdownBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryBreakdownBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun getRoot(): View {
        TODO("Not yet implemented")
    }
}
