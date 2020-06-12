package com.example.uscovidstatistics.views.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.databinding.BottomNavDialogFragmentBinding
import com.example.uscovidstatistics.recyclerview.NavRecyclerView
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.views.navigation.BaseActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import es.dmoral.toasty.Toasty
import java.lang.Exception

class BottomDialog : BottomSheetDialogFragment(), ViewBinding{

    private lateinit var recyclerViewData: NavRecyclerView

    fun newInstance() : BottomDialog {
        return BottomDialog()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = BottomNavDialogFragmentBinding.inflate(inflater)
        recyclerViewData = NavRecyclerView(binding.root.context, newInstance(), binding.navigationRecycler)
        choicesListeners(binding)
        return binding.root
    }

    private fun choicesListeners(binding: BottomNavDialogFragmentBinding) {
        binding.settingsNa.setOnClickListener{
            changeViews(binding,View.GONE, View.VISIBLE)
            recyclerViewData.displayChoices("North America")
        }

        binding.settingsSa.setOnClickListener{
            changeViews(binding,View.GONE, View.VISIBLE)
            recyclerViewData.displayChoices("South America")
        }

        binding.settingsEu.setOnClickListener {
            changeViews(binding,View.GONE, View.VISIBLE)
            recyclerViewData.displayChoices("Europe")
        }

        binding.settingsAs.setOnClickListener {
            changeViews(binding,View.GONE, View.VISIBLE)
            recyclerViewData.displayChoices("Asia")
        }

        binding.settingsAf.setOnClickListener {
            changeViews(binding,View.GONE, View.VISIBLE)
            recyclerViewData.displayChoices("Africa")
        }

        binding.settingsAu.setOnClickListener {
            changeViews(binding,View.GONE, View.VISIBLE)
            recyclerViewData.displayChoices("Australia/Oceania")
        }

        binding.settingsBackBtn.setOnClickListener {
            changeViews(binding, View.VISIBLE, View.GONE)
        }
    }
    
    private fun changeViews(binding: BottomNavDialogFragmentBinding, showBase: Int, showExtended: Int) {
        binding.selectionLayout.visibility = showBase
        binding.countrySelection.visibility = showExtended
    }

    fun getContinentList(): HashMap<String, Array<String>> {
        return AppUtils().continentCountryList()
    }

    override fun getRoot(): View {
        TODO("Not yet implemented")
    }
}