package com.example.uscovidstatistics.views.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.children
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.BottomNavDialogFragmentBinding
import com.example.uscovidstatistics.recyclerview.NavRecyclerView
import com.example.uscovidstatistics.utils.AppUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomDialog : BottomSheetDialogFragment(), ViewBinding{

    private lateinit var recyclerViewData: NavRecyclerView

    private lateinit var enterAnimation: Animation

    private lateinit var exitAnimation: Animation

    private var showExit = true

    fun newInstance() : BottomDialog {
        return BottomDialog()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = BottomNavDialogFragmentBinding.inflate(inflater)
        recyclerViewData = NavRecyclerView(binding.root.context, this, binding.navigationRecycler)
        enterAnimation =  AnimationUtils.loadAnimation(binding.root.context, R.anim.enter_left)
        exitAnimation = AnimationUtils.loadAnimation(binding.root.context, R.anim.exit_right)
        setAnimations(binding)
        choicesListeners(binding)
        return binding.root
    }

    private fun choicesListeners(binding: BottomNavDialogFragmentBinding) {
        binding.settingsNa.setOnClickListener{
            translateViews(binding.countrySelection, true)
            recyclerViewData.displayChoices("North America")
        }

        binding.settingsSa.setOnClickListener{
            translateViews(binding.countrySelection, true)
            recyclerViewData.displayChoices("South America")
        }

        binding.settingsEu.setOnClickListener {
            translateViews(binding.countrySelection, true)
            recyclerViewData.displayChoices("Europe")
        }

        binding.settingsAs.setOnClickListener {
            translateViews(binding.countrySelection, true)
            recyclerViewData.displayChoices("Asia")
        }

        binding.settingsAf.setOnClickListener {
            translateViews(binding.countrySelection, true)
            recyclerViewData.displayChoices("Africa")
        }

        binding.settingsAu.setOnClickListener {
            translateViews(binding.countrySelection, true)
            recyclerViewData.displayChoices("Australia/Oceania")
        }

        binding.settingsBackBtn.setOnClickListener {
            translateViews(binding.countrySelection, false)
        }
    }

    private fun setAnimations(binding: BottomNavDialogFragmentBinding) {
        exitAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                binding.countrySelection.translationX = 1600f
            }

            override fun onAnimationStart(animation: Animation) {
                AppConstants.RECYCLER_CLICKABLE = false
            }
        })

        enterAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                AppConstants.RECYCLER_CLICKABLE = true
            }

            override fun onAnimationStart(animation: Animation) {}
        })
    }

    private fun translateViews(view: View, enter: Boolean) {
        if (enter) {
            view.startAnimation(enterAnimation)
            view.translationX = 0f
        } else {
            view.startAnimation(exitAnimation)
        }
    }

    fun getContinentList(): HashMap<String, Array<String>> {
        return AppUtils().continentCountryList()
    }

    override fun getRoot(): View {
        TODO("Not yet implemented")
    }
}