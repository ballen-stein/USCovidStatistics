package com.example.uscovidstatistics.views.dialogs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.databinding.BottomNavDialogFragmentBinding
import com.example.uscovidstatistics.recyclerview.NavRecyclerView
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.views.activities.homepage.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomDialog(private val mContext: Context) : BottomSheetDialogFragment(), ViewBinding{

    private lateinit var recyclerViewData: NavRecyclerView

    private lateinit var enterAnimation: Animation

    private lateinit var exitAnimation: Animation

    private lateinit var entAnim: TranslateAnimation
    private lateinit var extAnim: TranslateAnimation

    fun newInstance() : BottomDialog {
        return BottomDialog(mContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = BottomNavDialogFragmentBinding.inflate(inflater)
        recyclerViewData = NavRecyclerView(binding.root.context, this, binding.navigationRecycler)
        enterAnimation =  AnimationUtils.loadAnimation(binding.root.context, R.anim.enter_right)
        exitAnimation = AnimationUtils.loadAnimation(binding.root.context, R.anim.exit_right)
        setTranslateAnimations(binding)
        choicesListeners(binding)
        return binding.root
    }

    private fun choicesListeners(binding: BottomNavDialogFragmentBinding) {

        binding.menuHome.setOnClickListener {
            val intent = Intent(mContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            (mContext as Activity).overridePendingTransition(R.anim.enter_right, R.anim.exit_left)

        }

        binding.menuLocation.setOnClickListener{
            translateViews(binding.selectionLayout, binding.baseLayout, true)
        }

        binding.selectionBackButton.setOnClickListener {
            translateViews(binding.selectionLayout, binding.baseLayout, false)
        }

        binding.settingsNa.setOnClickListener{
            translateViews(binding.countrySelection, binding.selectionLayout, true)
            recyclerViewData.displayChoices("North America")
        }

        binding.settingsSa.setOnClickListener{
            translateViews(binding.countrySelection, binding.selectionLayout, true)
            recyclerViewData.displayChoices("South America")
        }

        binding.settingsEu.setOnClickListener {
            translateViews(binding.countrySelection, binding.selectionLayout, true)
            recyclerViewData.displayChoices("Europe")
        }

        binding.settingsAs.setOnClickListener {
            translateViews(binding.countrySelection, binding.selectionLayout, true)
            recyclerViewData.displayChoices("Asia")
        }

        binding.settingsAf.setOnClickListener {
            translateViews(binding.countrySelection, binding.selectionLayout, true)
            recyclerViewData.displayChoices("Africa")
        }

        binding.settingsAu.setOnClickListener {
            translateViews(binding.countrySelection, binding.selectionLayout, true)
            recyclerViewData.displayChoices("Australia/Oceania")
        }

        binding.settingsBackBtn.setOnClickListener {
            translateViews(binding.countrySelection,  binding.selectionLayout, false)
        }
    }


    private fun setTranslateAnimations(binding: BottomNavDialogFragmentBinding) {
        entAnim = TranslateAnimation(1600f, 0f, 0f, 0f)
        extAnim = TranslateAnimation(0f, 1600f, 0f, 0f)
        entAnim.duration = 750
        extAnim.duration = 750

        extAnim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(p0: Animation?) {
                TODO("Not yet implemented")
            }

            override fun onAnimationEnd(p0: Animation?) {
                if (binding.countrySelection.translationX < 400f) {
                    binding.selectionLayout.clearAnimation()
                    binding.countrySelection.clearAnimation()
                    binding.selectionLayout.translationX = 0f
                    binding.countrySelection.translationX = 1600f
                }
                else {
                    binding.baseLayout.clearAnimation()
                    binding.selectionLayout.clearAnimation()
                    binding.baseLayout.translationX = 0f
                    binding.selectionLayout.translationX = 3600f
                }
            }

            override fun onAnimationStart(p0: Animation?) {
                AppConstants.RECYCLER_CLICKABLE = false
            }

        })

        entAnim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                AppConstants.RECYCLER_CLICKABLE = true
            }

            override fun onAnimationStart(p0: Animation?) {
            }

        })
    }

    private fun setAnimations(binding: BottomNavDialogFragmentBinding) {
        exitAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                if (binding.countrySelection.translationX < 400f) {
                    binding.selectionLayout.translationX = 20f
                    binding.countrySelection.translationX = 1600f
                }
                else {
                    binding.baseLayout.translationX = 0f
                    binding.selectionLayout.translationX = 3600f
                }
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

    private fun translateViews(view: View, view2: View, enter: Boolean) {
        if (enter) {
            view.startAnimation(entAnim)
            view2.startAnimation(entAnim)
            view.translationX = 0f
            view2.translationX = -1600f
        } else {
            view.startAnimation(extAnim)
            view2.startAnimation(extAnim)
        }
    }

    fun getContinentList(): HashMap<String, Array<String>> {
        return AppUtils().continentCountryList()
    }

    override fun getRoot(): View {
        TODO("Not yet implemented")
    }
}