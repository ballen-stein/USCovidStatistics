package com.example.uscovidstatistics.views.dialogs

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.databinding.CountryListDialogFragmentBinding
import com.example.uscovidstatistics.recyclerview.NavRecyclerView
import com.example.uscovidstatistics.utils.AppUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.country_list_dialog_fragment.*

class CountryListDialog(private val mContext: Context, private val dialogFragment: SearchDialog, private val continentName: String): BottomSheetDialogFragment(), ViewBinding {

    private lateinit var recyclerViewData: NavRecyclerView

    private lateinit var binding: CountryListDialogFragmentBinding

    fun newInstance(): CountryListDialog {
        return CountryListDialog(mContext, dialogFragment, continentName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = CountryListDialogFragmentBinding.inflate(inflater)
        val countryHeaderText = "Countries in ${continentName}"
        binding.countryListHeader.text = countryHeaderText
        recyclerViewData = NavRecyclerView(mContext as Activity, this, binding.navigationRecycler)
        recyclerViewData.displayChoices(continentName)
        setBackListener()
        return root
    }

    private fun setBackListener() {
        binding.settingsBackBtn.setOnClickListener {
            super.dismiss()
        }
    }

    override fun dismiss() {
        dialogFragment.dismiss()
        super.dismiss()
    }

    fun getContinentList(): HashMap<String, Array<String>> {
        return AppUtils().continentCountryList()
    }

    override fun getRoot(): View {
        return binding.root
    }
}